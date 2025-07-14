package com.whatsapp.service;

import com.whatsapp.dto.MessageEvent;
import com.whatsapp.dto.NotificationEvent;
import com.whatsapp.dto.UserEvent;

public interface KafkaService {
    void publishMessageEvent(MessageEvent event);
    void publishUserEvent(UserEvent event);
    void publishNotificationEvent(NotificationEvent event);
} 