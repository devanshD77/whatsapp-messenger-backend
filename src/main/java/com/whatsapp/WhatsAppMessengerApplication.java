package com.whatsapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class WhatsAppMessengerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WhatsAppMessengerApplication.class, args);
    }
} 