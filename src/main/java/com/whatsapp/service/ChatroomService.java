package com.whatsapp.service;

import com.whatsapp.dto.ChatroomDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ChatroomService {

    ChatroomDto createChatroom(Long user1Id, Long user2Id);
    
    Optional<ChatroomDto> getChatroomById(Long chatroomId);
    
    Optional<ChatroomDto> getChatroomByUsers(Long user1Id, Long user2Id);
    
    Page<ChatroomDto> getUserChatrooms(Long userId, Pageable pageable);
    
    boolean existsByUsers(Long user1Id, Long user2Id);
    
    void deleteChatroom(Long chatroomId);
} 