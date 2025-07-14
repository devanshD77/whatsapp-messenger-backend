package com.whatsapp.dto;

import com.whatsapp.model.Message;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public class MessageDto {

    private Long id;
    
    @NotBlank(message = "Message content is required")
    private String content;
    
    @NotNull(message = "Message type is required")
    private Message.MessageType messageType;
    
    private UserDto sender;
    private Long chatroomId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ReactionDto> reactions;
    private List<AttachmentDto> attachments;

    // Constructors
    public MessageDto() {}

    public MessageDto(Long id, String content, Message.MessageType messageType, 
                     UserDto sender, Long chatroomId, LocalDateTime createdAt, 
                     LocalDateTime updatedAt) {
        this.id = id;
        this.content = content;
        this.messageType = messageType;
        this.sender = sender;
        this.chatroomId = chatroomId;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Message.MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(Message.MessageType messageType) {
        this.messageType = messageType;
    }

    public UserDto getSender() {
        return sender;
    }

    public void setSender(UserDto sender) {
        this.sender = sender;
    }

    public Long getChatroomId() {
        return chatroomId;
    }

    public void setChatroomId(Long chatroomId) {
        this.chatroomId = chatroomId;
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

    public List<ReactionDto> getReactions() {
        return reactions;
    }

    public void setReactions(List<ReactionDto> reactions) {
        this.reactions = reactions;
    }

    public List<AttachmentDto> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentDto> attachments) {
        this.attachments = attachments;
    }

    @Override
    public String toString() {
        return "MessageDto{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", messageType=" + messageType +
                ", sender=" + sender +
                ", chatroomId=" + chatroomId +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", reactions=" + reactions +
                ", attachments=" + attachments +
                '}';
    }
} 