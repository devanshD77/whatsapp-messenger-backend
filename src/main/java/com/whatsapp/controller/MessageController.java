package com.whatsapp.controller;

import com.whatsapp.dto.MessageDto;
import com.whatsapp.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/messages")
@Tag(name = "Message Management", description = "APIs for sending and retrieving messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/text")
    @Operation(summary = "Send text message", description = "Sends a text message to a chatroom")
    public ResponseEntity<MessageDto> sendTextMessage(
            @Parameter(description = "Chatroom ID") @RequestParam Long chatroomId,
            @Parameter(description = "Sender ID") @RequestParam Long senderId,
            @Parameter(description = "Message content") @RequestParam String content) {
        MessageDto message = messageService.sendTextMessage(chatroomId, senderId, content);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @PostMapping("/attachment")
    @Operation(summary = "Send message with attachment", description = "Sends a message with file attachments (images/videos)")
    public ResponseEntity<MessageDto> sendMessageWithAttachment(
            @Parameter(description = "Chatroom ID") @RequestParam Long chatroomId,
            @Parameter(description = "Sender ID") @RequestParam Long senderId,
            @Parameter(description = "Message content") @RequestParam(required = false) String content,
            @Parameter(description = "File attachments") @RequestParam("files") List<MultipartFile> files) {
        MessageDto message = messageService.sendMessageWithAttachment(chatroomId, senderId, content, files);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @GetMapping("/{messageId}")
    @Operation(summary = "Get message by ID", description = "Retrieves a specific message by its ID")
    public ResponseEntity<MessageDto> getMessageById(
            @Parameter(description = "Message ID") @PathVariable Long messageId) {
        return messageService.getMessageById(messageId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/chatroom/{chatroomId}")
    @Operation(summary = "Get chatroom messages", description = "Retrieves paginated messages for a specific chatroom")
    public ResponseEntity<Page<MessageDto>> getChatroomMessages(
            @Parameter(description = "Chatroom ID") @PathVariable Long chatroomId,
            Pageable pageable) {
        Page<MessageDto> messages = messageService.getChatroomMessages(chatroomId, pageable);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/chatroom/{chatroomId}/before/{messageId}")
    @Operation(summary = "Get messages before ID", description = "Retrieves messages before a specific message ID")
    public ResponseEntity<List<MessageDto>> getMessagesBeforeId(
            @Parameter(description = "Chatroom ID") @PathVariable Long chatroomId,
            @Parameter(description = "Message ID") @PathVariable Long messageId,
            @Parameter(description = "Number of messages to retrieve") @RequestParam(defaultValue = "20") int limit) {
        List<MessageDto> messages = messageService.getChatroomMessagesBeforeId(chatroomId, messageId, limit);
        return ResponseEntity.ok(messages);
    }

    @DeleteMapping("/{messageId}")
    @Operation(summary = "Delete message", description = "Deletes a message (only by the sender)")
    public ResponseEntity<Void> deleteMessage(
            @Parameter(description = "Message ID") @PathVariable Long messageId,
            @Parameter(description = "User ID (sender)") @RequestParam Long userId) {
        messageService.deleteMessage(messageId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/chatroom/{chatroomId}/count")
    @Operation(summary = "Get message count", description = "Retrieves the total number of messages in a chatroom")
    public ResponseEntity<Long> getMessageCount(
            @Parameter(description = "Chatroom ID") @PathVariable Long chatroomId) {
        long count = messageService.getMessageCountByChatroom(chatroomId);
        return ResponseEntity.ok(count);
    }

    @PutMapping("/{messageId}")
    @Operation(summary = "Edit message", description = "Edits a message (only by the sender within 15 minutes)")
    public ResponseEntity<MessageDto> editMessage(
            @Parameter(description = "Message ID") @PathVariable Long messageId,
            @Parameter(description = "User ID (sender)") @RequestParam Long userId,
            @Parameter(description = "New message content") @RequestParam String newContent) {
        MessageDto message = messageService.editMessage(messageId, userId, newContent);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/chatroom/{chatroomId}/search")
    @Operation(summary = "Search messages", description = "Searches for messages containing specific text in a chatroom")
    public ResponseEntity<List<MessageDto>> searchMessages(
            @Parameter(description = "Chatroom ID") @PathVariable Long chatroomId,
            @Parameter(description = "Search term") @RequestParam String searchTerm) {
        List<MessageDto> messages = messageService.searchMessages(chatroomId, searchTerm);
        return ResponseEntity.ok(messages);
    }
} 