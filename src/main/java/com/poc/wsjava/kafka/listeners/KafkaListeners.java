package com.poc.wsjava.kafka.listeners;

import com.poc.wsjava.kafka.messages.EventMessage;
import com.poc.wsjava.service.AgentSenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {
    private final Logger logger = LoggerFactory.getLogger(KafkaListeners.class);
    @Autowired
    private AgentSenderService agentService;

    @KafkaListener(topics = "events", groupId = "event_group_id")
    void eventListener(@Payload EventMessage data){
        logger.info("Event received: [ " + data.getEventDetail() + " ]..");
        agentService.deliverAgentEvents(data);
    }

    @KafkaListener(topics = "notifications", groupId = "notification_group_id")
    void notificationListener(@Payload EventMessage data){
        logger.info("Notification received: [ " + data + " ]..");
    }
}
