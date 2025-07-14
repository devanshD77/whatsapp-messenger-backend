package com.whatsapp.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whatsapp.dto.MessageEvent;
import com.whatsapp.dto.NotificationEvent;
import com.whatsapp.dto.UserEvent;
import com.whatsapp.service.KafkaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "spring.kafka.bootstrap-servers")
public class KafkaServiceImpl implements KafkaService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaServiceImpl.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${kafka.topic.message-events:message-events}")
    private String messageEventsTopic;

    @Value("${kafka.topic.user-events:user-events}")
    private String userEventsTopic;

    @Value("${kafka.topic.notification-events:notification-events}")
    private String notificationEventsTopic;

    public KafkaServiceImpl(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publishMessageEvent(MessageEvent event) {
        try {
            String eventJson = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(messageEventsTopic, event.getEventType(), eventJson);
            logger.info("Published message event: {}", event.getEventType());
        } catch (JsonProcessingException e) {
            logger.error("Error serializing message event", e);
        } catch (Exception e) {
            logger.warn("Kafka not available, skipping message event: {}", event.getEventType());
        }
    }

    @Override
    public void publishUserEvent(UserEvent event) {
        try {
            String eventJson = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(userEventsTopic, event.getEventType(), eventJson);
            logger.info("Published user event: {}", event.getEventType());
        } catch (JsonProcessingException e) {
            logger.error("Error serializing user event", e);
        } catch (Exception e) {
            logger.warn("Kafka not available, skipping user event: {}", event.getEventType());
        }
    }

    @Override
    public void publishNotificationEvent(NotificationEvent event) {
        try {
            String eventJson = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(notificationEventsTopic, event.getEventType(), eventJson);
            logger.info("Published notification event: {}", event.getEventType());
        } catch (JsonProcessingException e) {
            logger.error("Error serializing notification event", e);
        } catch (Exception e) {
            logger.warn("Kafka not available, skipping notification event: {}", event.getEventType());
        }
    }
} 