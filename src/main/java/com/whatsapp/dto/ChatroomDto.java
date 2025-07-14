package com.whatsapp.dto;

import java.time.LocalDateTime;

public class ChatroomDto {

    private Long id;
    private UserDto user1;
    private UserDto user2;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private MessageDto lastMessage;

    // Constructors
    public ChatroomDto() {}

    public ChatroomDto(Long id, UserDto user1, UserDto user2, 
                       LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.user1 = user1;
        this.user2 = user2;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDto getUser1() {
        return user1;
    }

    public void setUser1(UserDto user1) {
        this.user1 = user1;
    }

    public UserDto getUser2() {
        return user2;
    }

    public void setUser2(UserDto user2) {
        this.user2 = user2;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public MessageDto getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(MessageDto lastMessage) {
        this.lastMessage = lastMessage;
    }

    @Override
    public String toString() {
        return "ChatroomDto{" +
                "id=" + id +
                ", user1=" + user1 +
                ", user2=" + user2 +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", lastMessage=" + lastMessage +
                '}';
    }
} 