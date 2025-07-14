package com.whatsapp.service;

import com.whatsapp.dto.MessageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface MessageService {

    MessageDto sendTextMessage(Long chatroomId, Long senderId, String content);
    
    MessageDto sendMessageWithAttachment(Long chatroomId, Long senderId, String content, 
                                       List<MultipartFile> files);
    
    Optional<MessageDto> getMessageById(Long messageId);
    
    Page<MessageDto> getChatroomMessages(Long chatroomId, Pageable pageable);
    
    List<MessageDto> getChatroomMessagesBeforeId(Long chatroomId, Long messageId, int limit);
    
    void deleteMessage(Long messageId, Long userId);
    
    MessageDto editMessage(Long messageId, Long userId, String newContent);
    
    long getMessageCountByChatroom(Long chatroomId);
    
    List<MessageDto> searchMessages(Long chatroomId, String searchTerm);
} 