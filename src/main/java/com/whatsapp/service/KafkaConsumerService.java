package com.whatsapp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whatsapp.dto.MessageEvent;
import com.whatsapp.dto.NotificationEvent;
import com.whatsapp.dto.UserEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "spring.kafka.bootstrap-servers")
public class KafkaConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    private final ObjectMapper objectMapper;

    @Value("${kafka.topic.message-events:message-events}")
    private String messageEventsTopic;

    @Value("${kafka.topic.user-events:user-events}")
    private String userEventsTopic;

    @Value("${kafka.topic.notification-events:notification-events}")
    private String notificationEventsTopic;

    public KafkaConsumerService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "${kafka.topic.message-events:message-events}", 
                   groupId = "${spring.kafka.consumer.group-id:whatsapp-group}")
    public void consumeMessageEvents(String eventJson) {
        try {
            MessageEvent event = objectMapper.readValue(eventJson, MessageEvent.class);
            logger.info("Consumed message event: {} from user: {}", 
                       event.getEventType(), event.getSenderUsername());
            
            // Handle different message event types
            switch (event.getEventType()) {
                case "MESSAGE_SENT":
                    handleMessageSent(event);
                    break;
                case "MESSAGE_DELETED":
                    handleMessageDeleted(event);
                    break;
                case "MESSAGE_EDITED":
                    handleMessageEdited(event);
                    break;
                default:
                    logger.warn("Unknown message event type: {}", event.getEventType());
            }
        } catch (Exception e) {
            logger.error("Error processing message event: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "${kafka.topic.user-events:user-events}", 
                   groupId = "${spring.kafka.consumer.group-id:whatsapp-group}")
    public void consumeUserEvents(String eventJson) {
        try {
            UserEvent event = objectMapper.readValue(eventJson, UserEvent.class);
            logger.info("Consumed user event: {} for user: {}", 
                       event.getEventType(), event.getUsername());
            
            // Handle different user event types
            switch (event.getEventType()) {
                case "USER_ONLINE":
                    handleUserOnline(event);
                    break;
                case "USER_OFFLINE":
                    handleUserOffline(event);
                    break;
                case "USER_STATUS_CHANGED":
                    handleUserStatusChanged(event);
                    break;
                case "USER_PROFILE_UPDATED":
                    handleUserProfileUpdated(event);
                    break;
                default:
                    logger.warn("Unknown user event type: {}", event.getEventType());
            }
        } catch (Exception e) {
            logger.error("Error processing user event: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "${kafka.topic.notification-events:notification-events}", 
                   groupId = "${spring.kafka.consumer.group-id:whatsapp-group}")
    public void consumeNotificationEvents(String eventJson) {
        try {
            NotificationEvent event = objectMapper.readValue(eventJson, NotificationEvent.class);
            logger.info("Consumed notification event: {} for user: {}", 
                       event.getEventType(), event.getRecipientUsername());
            
            // Handle different notification event types
            switch (event.getEventType()) {
                case "NEW_MESSAGE":
                    handleNewMessageNotification(event);
                    break;
                case "MESSAGE_REACTION":
                    handleMessageReactionNotification(event);
                    break;
                case "USER_MENTION":
                    handleUserMentionNotification(event);
                    break;
                case "STATUS_UPDATE":
                    handleStatusUpdateNotification(event);
                    break;
                default:
                    logger.warn("Unknown notification event type: {}", event.getEventType());
            }
        } catch (Exception e) {
            logger.error("Error processing notification event: {}", e.getMessage(), e);
        }
    }

    // Message event handlers
    private void handleMessageSent(MessageEvent event) {
        // Update message delivery status, send real-time notifications, etc.
        logger.info("Message sent: {} by user: {} in chatroom: {}", 
                   event.getMessageId(), event.getSenderUsername(), event.getChatroomId());
    }

    private void handleMessageDeleted(MessageEvent event) {
        // Update UI, notify other users about message deletion, etc.
        logger.info("Message deleted: {} by user: {} in chatroom: {}", 
                   event.getMessageId(), event.getSenderUsername(), event.getChatroomId());
    }

    private void handleMessageEdited(MessageEvent event) {
        // Update UI, notify other users about message edit, etc.
        logger.info("Message edited: {} by user: {} in chatroom: {}", 
                   event.getMessageId(), event.getSenderUsername(), event.getChatroomId());
    }

    // User event handlers
    private void handleUserOnline(UserEvent event) {
        // Update user status, notify contacts, update UI, etc.
        logger.info("User online: {} with status: {}", event.getUsername(), event.getStatus());
    }

    private void handleUserOffline(UserEvent event) {
        // Update user status, notify contacts, update UI, etc.
        logger.info("User offline: {} with status: {}", event.getUsername(), event.getStatus());
    }

    private void handleUserStatusChanged(UserEvent event) {
        // Update user status, notify contacts, update UI, etc.
        logger.info("User status changed: {} from {} to {}", 
                   event.getUsername(), event.getOldStatus(), event.getNewStatus());
    }

    private void handleUserProfileUpdated(UserEvent event) {
        // Update user profile, notify contacts, update UI, etc.
        logger.info("User profile updated: {} field: {}", event.getUsername(), event.getUpdatedField());
    }

    // Notification event handlers
    private void handleNewMessageNotification(NotificationEvent event) {
        // Send push notification, email, in-app notification, etc.
        logger.info("New message notification for: {} from: {} in chatroom: {}", 
                   event.getRecipientUsername(), event.getSenderUsername(), event.getChatroomId());
    }

    private void handleMessageReactionNotification(NotificationEvent event) {
        // Send notification about message reaction
        logger.info("Message reaction notification for: {} from: {} on message: {}", 
                   event.getRecipientUsername(), event.getSenderUsername(), event.getMessageId());
    }

    private void handleUserMentionNotification(NotificationEvent event) {
        // Send notification about user mention
        logger.info("User mention notification for: {} from: {} in message: {}", 
                   event.getRecipientUsername(), event.getSenderUsername(), event.getMessageId());
    }

    private void handleStatusUpdateNotification(NotificationEvent event) {
        // Send notification about status update
        logger.info("Status update notification for: {} from: {}", 
                   event.getRecipientUsername(), event.getSenderUsername());
    }
} 