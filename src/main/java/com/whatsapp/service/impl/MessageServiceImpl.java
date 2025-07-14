package com.whatsapp.service.impl;

import com.whatsapp.dto.AttachmentDto;
import com.whatsapp.dto.MessageDto;
import com.whatsapp.dto.MessageEvent;
import com.whatsapp.dto.NotificationEvent;
import com.whatsapp.dto.UserDto;
import com.whatsapp.model.Attachment;
import com.whatsapp.model.Chatroom;
import com.whatsapp.model.Message;
import com.whatsapp.model.User;
import com.whatsapp.repository.AttachmentRepository;
import com.whatsapp.repository.ChatroomRepository;
import com.whatsapp.repository.MessageRepository;
import com.whatsapp.repository.UserRepository;
import com.whatsapp.service.KafkaService;
import com.whatsapp.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChatroomRepository chatroomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private KafkaService kafkaService;

    @Value("${app.file.picture-dir}")
    private String pictureDir;

    @Value("${app.file.video-dir}")
    private String videoDir;

    @Override
    public MessageDto sendTextMessage(Long chatroomId, Long senderId, String content) {
        // Validate chatroom and sender
        Chatroom chatroom = validateChatroomAndSender(chatroomId, senderId);
        User sender = userRepository.findById(senderId).get();

        // Create and save message
        Message message = new Message(content, Message.MessageType.TEXT, sender, chatroom);
        Message savedMessage = messageRepository.save(message);
        
        // Update chatroom's updatedAt timestamp
        updateChatroomTimestamp(chatroom);
        
        // Publish message event to Kafka
        publishMessageEvent(savedMessage, "MESSAGE_SENT");
        
        // Send notifications to other users in chatroom
        sendMessageNotifications(savedMessage, chatroom, sender);
        
        return convertToDto(savedMessage);
    }

    @Override
    public MessageDto sendMessageWithAttachment(Long chatroomId, Long senderId, String content, 
                                             List<MultipartFile> files) {
        // Validate chatroom and sender
        Chatroom chatroom = validateChatroomAndSender(chatroomId, senderId);
        User sender = userRepository.findById(senderId).get();

        // Validate file size and type
        validateFiles(files);

        Message.MessageType messageType = files.stream()
                .anyMatch(file -> file.getContentType().startsWith("video/")) 
                ? Message.MessageType.VIDEO : Message.MessageType.IMAGE;

        Message message = new Message(content, messageType, sender, chatroom);
        Message savedMessage = messageRepository.save(message);

        // Save attachments
        List<Attachment> attachments = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                Attachment attachment = saveAttachment(file, savedMessage);
                attachments.add(attachment);
            } catch (IOException e) {
                throw new RuntimeException("Failed to save attachment: " + e.getMessage());
            }
        }
        savedMessage.setAttachments(attachments);

        // Update chatroom's updatedAt timestamp
        updateChatroomTimestamp(chatroom);

        // Publish message event to Kafka
        publishMessageEvent(savedMessage, "MESSAGE_SENT");
        
        // Send notifications to other users in chatroom
        sendMessageNotifications(savedMessage, chatroom, sender);

        return convertToDto(savedMessage);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MessageDto> getMessageById(Long messageId) {
        return messageRepository.findById(messageId)
                .map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MessageDto> getChatroomMessages(Long chatroomId, Pageable pageable) {
        Chatroom chatroom = chatroomRepository.findById(chatroomId)
                .orElseThrow(() -> new RuntimeException("Chatroom not found with id: " + chatroomId));

        return messageRepository.findMessagesByChatroom(chatroom, pageable)
                .map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MessageDto> getChatroomMessagesBeforeId(Long chatroomId, Long messageId, int limit) {
        Chatroom chatroom = chatroomRepository.findById(chatroomId)
                .orElseThrow(() -> new RuntimeException("Chatroom not found with id: " + chatroomId));

        Pageable pageable = PageRequest.of(0, limit);
        return messageRepository.findMessagesBeforeId(chatroom, messageId, pageable)
                .getContent()
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    public void deleteMessage(Long messageId, Long userId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found with id: " + messageId));

        if (!message.getSender().getId().equals(userId)) {
            throw new RuntimeException("User can only delete their own messages");
        }

        // Delete attachments from file system
        for (Attachment attachment : message.getAttachments()) {
            try {
                Files.deleteIfExists(Paths.get(attachment.getFilePath()));
            } catch (IOException e) {
                // Log error but continue with deletion
                System.err.println("Failed to delete file: " + attachment.getFilePath());
            }
        }

        // Publish message deletion event
        publishMessageEvent(message, "MESSAGE_DELETED");

        messageRepository.deleteById(messageId);
    }

    @Override
    public MessageDto editMessage(Long messageId, Long userId, String newContent) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found with id: " + messageId));

        if (!message.getSender().getId().equals(userId)) {
            throw new RuntimeException("User can only edit their own messages");
        }

        // Check if message is within editable time window (e.g., 15 minutes)
        if (message.getCreatedAt().plusMinutes(15).isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Message can only be edited within 15 minutes of sending");
        }

        message.setContent(newContent);
        message.setUpdatedAt(LocalDateTime.now());
        Message savedMessage = messageRepository.save(message);

        // Publish message edit event
        publishMessageEvent(savedMessage, "MESSAGE_EDITED");

        return convertToDto(savedMessage);
    }

    @Override
    @Transactional(readOnly = true)
    public long getMessageCountByChatroom(Long chatroomId) {
        Chatroom chatroom = chatroomRepository.findById(chatroomId)
                .orElseThrow(() -> new RuntimeException("Chatroom not found with id: " + chatroomId));

        return messageRepository.countByChatroom(chatroom);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MessageDto> searchMessages(Long chatroomId, String searchTerm) {
        Chatroom chatroom = chatroomRepository.findById(chatroomId)
                .orElseThrow(() -> new RuntimeException("Chatroom not found with id: " + chatroomId));

        return messageRepository.findByChatroomAndContentContainingIgnoreCase(chatroom, searchTerm)
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    // Private helper methods
    private Chatroom validateChatroomAndSender(Long chatroomId, Long senderId) {
        Chatroom chatroom = chatroomRepository.findById(chatroomId)
                .orElseThrow(() -> new RuntimeException("Chatroom not found with id: " + chatroomId));
        
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + senderId));

        // Check if user is part of the chatroom
        if (!chatroom.getUser1().getId().equals(senderId) && !chatroom.getUser2().getId().equals(senderId)) {
            throw new RuntimeException("User is not part of this chatroom");
        }

        return chatroom;
    }

    private void validateFiles(List<MultipartFile> files) {
        for (MultipartFile file : files) {
            if (file.getSize() > 10 * 1024 * 1024) { // 10MB limit
                throw new RuntimeException("File size exceeds 10MB limit: " + file.getOriginalFilename());
            }
            
            String contentType = file.getContentType();
            if (contentType == null || (!contentType.startsWith("image/") && !contentType.startsWith("video/"))) {
                throw new RuntimeException("Invalid file type. Only images and videos are allowed: " + file.getOriginalFilename());
            }
        }
    }

    private void updateChatroomTimestamp(Chatroom chatroom) {
        chatroom.setUpdatedAt(LocalDateTime.now());
        chatroomRepository.save(chatroom);
    }

    private void publishMessageEvent(Message message, String eventType) {
        MessageEvent event = new MessageEvent();
        event.setEventType(eventType);
        event.setMessageId(message.getId());
        event.setChatroomId(message.getChatroom().getId());
        event.setSenderUsername(message.getSender().getUsername());
        event.setContent(message.getContent());
        event.setMessageType(message.getMessageType().toString());
        event.setTimestamp(LocalDateTime.now());
        
        if (!message.getAttachments().isEmpty()) {
            event.setAttachmentUrl(message.getAttachments().get(0).getFilePath());
        }
        
        kafkaService.publishMessageEvent(event);
    }

    private void sendMessageNotifications(Message message, Chatroom chatroom, User sender) {
        User recipient = chatroom.getUser1().getId().equals(sender.getId()) ? 
                        chatroom.getUser2() : chatroom.getUser1();

        NotificationEvent notification = new NotificationEvent();
        notification.setEventType("NEW_MESSAGE");
        notification.setRecipientUsername(recipient.getUsername());
        notification.setSenderUsername(sender.getUsername());
        notification.setNotificationType("PUSH");
        notification.setTitle("New message from " + sender.getUsername());
        notification.setMessage(message.getContent().length() > 50 ? 
                             message.getContent().substring(0, 50) + "..." : message.getContent());
        notification.setChatroomId(chatroom.getId().toString());
        notification.setMessageId(message.getId().toString());
        notification.setTimestamp(LocalDateTime.now());

        kafkaService.publishNotificationEvent(notification);
    }

    private Attachment saveAttachment(MultipartFile file, Message message) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString() + fileExtension;
        
        String uploadDir = file.getContentType().startsWith("video/") ? videoDir : pictureDir;
        Path uploadPath = Paths.get(uploadDir);
        
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);
        
        Attachment.AttachmentType attachmentType = file.getContentType().startsWith("video/") 
                ? Attachment.AttachmentType.VIDEO : Attachment.AttachmentType.IMAGE;
        
        Attachment attachment = new Attachment(
                fileName,
                filePath.toString(),
                file.getContentType(),
                file.getSize(),
                attachmentType,
                message
        );
        
        return attachmentRepository.save(attachment);
    }

    private MessageDto convertToDto(Message message) {
        UserDto senderDto = convertUserToDto(message.getSender());
        
        List<AttachmentDto> attachmentDtos = message.getAttachments().stream()
                .map(this::convertAttachmentToDto)
                .toList();

        return new MessageDto(
                message.getId(),
                message.getContent(),
                message.getMessageType(),
                senderDto,
                message.getChatroom().getId(),
                message.getCreatedAt(),
                message.getUpdatedAt()
        );
    }

    private UserDto convertUserToDto(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getBio(),
                user.getAvatarUrl(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    private AttachmentDto convertAttachmentToDto(Attachment attachment) {
        return new AttachmentDto(
                attachment.getId(),
                attachment.getFileName(),
                attachment.getFilePath(),
                attachment.getFileType(),
                attachment.getFileSize(),
                attachment.getAttachmentType(),
                attachment.getMessage().getId(),
                attachment.getCreatedAt()
        );
    }
} 