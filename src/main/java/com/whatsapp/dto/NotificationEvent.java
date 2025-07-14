package com.whatsapp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class NotificationEvent {
    private String eventType; // NEW_MESSAGE, MESSAGE_REACTION, USER_MENTION, STATUS_UPDATE
    private String recipientUsername;
    private String senderUsername;
    private String notificationType; // PUSH, EMAIL, IN_APP
    private String title;
    private String message;
    private String chatroomId;
    private String messageId;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    
    // For group notifications
    private List<String> recipientUsernames;
    private String groupName;
} 