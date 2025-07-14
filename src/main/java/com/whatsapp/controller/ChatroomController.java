package com.whatsapp.controller;

import com.whatsapp.dto.ChatroomDto;
import com.whatsapp.service.ChatroomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chatrooms")
@Tag(name = "Chatroom Management", description = "APIs for managing chatrooms")
public class ChatroomController {

    @Autowired
    private ChatroomService chatroomService;

    @PostMapping
    @Operation(summary = "Create or get chatroom", description = "Creates a new chatroom between two users or returns existing one")
    public ResponseEntity<ChatroomDto> createChatroom(
            @Parameter(description = "First user ID") @RequestParam Long user1Id,
            @Parameter(description = "Second user ID") @RequestParam Long user2Id) {
        ChatroomDto chatroom = chatroomService.createChatroom(user1Id, user2Id);
        return ResponseEntity.status(HttpStatus.CREATED).body(chatroom);
    }

    @GetMapping("/{chatroomId}")
    @Operation(summary = "Get chatroom by ID", description = "Retrieves a chatroom by its ID")
    public ResponseEntity<ChatroomDto> getChatroomById(
            @Parameter(description = "Chatroom ID") @PathVariable Long chatroomId) {
        return chatroomService.getChatroomById(chatroomId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/users")
    @Operation(summary = "Get chatroom by users", description = "Retrieves a chatroom between two specific users")
    public ResponseEntity<ChatroomDto> getChatroomByUsers(
            @Parameter(description = "First user ID") @RequestParam Long user1Id,
            @Parameter(description = "Second user ID") @RequestParam Long user2Id) {
        return chatroomService.getChatroomByUsers(user1Id, user2Id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get user's chatrooms", description = "Retrieves a paginated list of chatrooms for a specific user")
    public ResponseEntity<Page<ChatroomDto>> getUserChatrooms(
            @Parameter(description = "User ID") @PathVariable Long userId,
            Pageable pageable) {
        Page<ChatroomDto> chatrooms = chatroomService.getUserChatrooms(userId, pageable);
        return ResponseEntity.ok(chatrooms);
    }

    @GetMapping("/exists")
    @Operation(summary = "Check if chatroom exists", description = "Checks if a chatroom exists between two users")
    public ResponseEntity<Boolean> checkChatroomExists(
            @Parameter(description = "First user ID") @RequestParam Long user1Id,
            @Parameter(description = "Second user ID") @RequestParam Long user2Id) {
        boolean exists = chatroomService.existsByUsers(user1Id, user2Id);
        return ResponseEntity.ok(exists);
    }

    @DeleteMapping("/{chatroomId}")
    @Operation(summary = "Delete chatroom", description = "Deletes a chatroom")
    public ResponseEntity<Void> deleteChatroom(
            @Parameter(description = "Chatroom ID") @PathVariable Long chatroomId) {
        chatroomService.deleteChatroom(chatroomId);
        return ResponseEntity.noContent().build();
    }
} 