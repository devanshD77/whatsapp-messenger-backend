package com.whatsapp.service.impl;

import com.whatsapp.dto.ChatroomDto;
import com.whatsapp.dto.UserDto;
import com.whatsapp.model.Chatroom;
import com.whatsapp.model.User;
import com.whatsapp.repository.ChatroomRepository;
import com.whatsapp.repository.UserRepository;
import com.whatsapp.service.ChatroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ChatroomServiceImpl implements ChatroomService {

    @Autowired
    private ChatroomRepository chatroomRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ChatroomDto createChatroom(Long user1Id, Long user2Id) {
        User user1 = userRepository.findById(user1Id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + user1Id));
        
        User user2 = userRepository.findById(user2Id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + user2Id));

        // Check if chatroom already exists
        Optional<Chatroom> existingChatroom = chatroomRepository.findByUsers(user1, user2);
        if (existingChatroom.isPresent()) {
            return convertToDto(existingChatroom.get());
        }

        Chatroom chatroom = new Chatroom(user1, user2);
        Chatroom savedChatroom = chatroomRepository.save(chatroom);
        return convertToDto(savedChatroom);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ChatroomDto> getChatroomById(Long chatroomId) {
        return chatroomRepository.findById(chatroomId)
                .map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ChatroomDto> getChatroomByUsers(Long user1Id, Long user2Id) {
        User user1 = userRepository.findById(user1Id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + user1Id));
        
        User user2 = userRepository.findById(user2Id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + user2Id));

        return chatroomRepository.findByUsers(user1, user2)
                .map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ChatroomDto> getUserChatrooms(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        return chatroomRepository.findByUserOrderByUpdatedAtDesc(user, pageable)
                .map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsers(Long user1Id, Long user2Id) {
        User user1 = userRepository.findById(user1Id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + user1Id));
        
        User user2 = userRepository.findById(user2Id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + user2Id));

        return chatroomRepository.countByUsers(user1, user2) > 0;
    }

    @Override
    public void deleteChatroom(Long chatroomId) {
        if (!chatroomRepository.existsById(chatroomId)) {
            throw new RuntimeException("Chatroom not found with id: " + chatroomId);
        }
        chatroomRepository.deleteById(chatroomId);
    }

    private ChatroomDto convertToDto(Chatroom chatroom) {
        UserDto user1Dto = convertUserToDto(chatroom.getUser1());
        UserDto user2Dto = convertUserToDto(chatroom.getUser2());
        
        return new ChatroomDto(
                chatroom.getId(),
                user1Dto,
                user2Dto,
                chatroom.getCreatedAt(),
                chatroom.getUpdatedAt()
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
} 