package com.whatsapp.dto;

import com.whatsapp.model.Reaction;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class ReactionDto {

    private Long id;
    
    @NotNull(message = "Reaction type is required")
    private Reaction.ReactionType reactionType;
    
    private UserDto user;
    private Long messageId;
    private LocalDateTime createdAt;

    // Constructors
    public ReactionDto() {}

    public ReactionDto(Long id, Reaction.ReactionType reactionType, 
                      UserDto user, Long messageId, LocalDateTime createdAt) {
        this.id = id;
        this.reactionType = reactionType;
        this.user = user;
        this.messageId = messageId;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Reaction.ReactionType getReactionType() {
        return reactionType;
    }

    public void setReactionType(Reaction.ReactionType reactionType) {
        this.reactionType = reactionType;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "ReactionDto{" +
                "id=" + id +
                ", reactionType=" + reactionType +
                ", user=" + user +
                ", messageId=" + messageId +
                ", createdAt=" + createdAt +
                '}';
    }
} 