package com.whatsapp.service.impl;

import com.whatsapp.dto.MessageEvent;
import com.whatsapp.dto.NotificationEvent;
import com.whatsapp.dto.UserEvent;
import com.whatsapp.service.KafkaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "spring.kafka.bootstrap-servers", havingValue = "false", matchIfMissing = true)
public class KafkaServiceFallbackImpl implements KafkaService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaServiceFallbackImpl.class);

    @Override
    public void publishMessageEvent(MessageEvent event) {
        logger.info("Kafka not configured, logging message event: {} from user: {}", 
                   event.getEventType(), event.getSenderUsername());
    }

    @Override
    public void publishUserEvent(UserEvent event) {
        logger.info("Kafka not configured, logging user event: {} for user: {}", 
                   event.getEventType(), event.getUsername());
    }

    @Override
    public void publishNotificationEvent(NotificationEvent event) {
        logger.info("Kafka not configured, logging notification event: {} for user: {}", 
                   event.getEventType(), event.getRecipientUsername());
    }
} 