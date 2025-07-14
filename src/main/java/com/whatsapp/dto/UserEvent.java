package com.whatsapp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserEvent {
    private String eventType; // USER_ONLINE, USER_OFFLINE, USER_STATUS_CHANGED, USER_PROFILE_UPDATED
    private String username;
    private String fullName;
    private String status; // ONLINE, OFFLINE, AWAY, BUSY
    private String email;
    private String phoneNumber;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    
    // For profile updates
    private String oldStatus;
    private String newStatus;
    private String updatedField;
} 