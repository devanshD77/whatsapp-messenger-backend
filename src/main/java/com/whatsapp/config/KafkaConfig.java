package com.whatsapp.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@ConditionalOnProperty(name = "spring.kafka.bootstrap-servers")
public class KafkaConfig {

    @Value("${kafka.topic.message-events:message-events}")
    private String messageEventsTopic;

    @Value("${kafka.topic.user-events:user-events}")
    private String userEventsTopic;

    @Value("${kafka.topic.notification-events:notification-events}")
    private String notificationEventsTopic;

    @Bean
    public NewTopic messageEventsTopic() {
        return TopicBuilder.name(messageEventsTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic userEventsTopic() {
        return TopicBuilder.name(userEventsTopic)
                .partitions(2)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic notificationEventsTopic() {
        return TopicBuilder.name(notificationEventsTopic)
                .partitions(2)
                .replicas(1)
                .build();
    }
} 