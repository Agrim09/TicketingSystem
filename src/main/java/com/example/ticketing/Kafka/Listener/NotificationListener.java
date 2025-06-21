package com.example.ticketing.Kafka.Listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {
    
    private static final Logger LOG = LogManager.getLogger(NotificationListener.class);

    @KafkaListener(topics = "notification-topic", groupId = "service-group")
    public void sendNotification(String message) {
        LOG.info("Received notification message: {}", message);

        if (message == null || message.isBlank()) {
            LOG.warn("Received empty or null message, ignoring...");
            return;
        }

        LOG.info("Sending notification: {}", message);
    }
}
