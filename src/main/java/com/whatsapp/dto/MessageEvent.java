package com.whatsapp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageEvent {
    private String eventType; // MESSAGE_SENT, MESSAGE_DELETED, MESSAGE_EDITED
    private Long messageId;
    private Long chatroomId;
    private String senderUsername;
    private String content;
    private String messageType; // TEXT, IMAGE, VIDEO, AUDIO
    private String attachmentUrl;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    
    // For reactions
    private String reactionType;
    private String reactorUsername;
} 