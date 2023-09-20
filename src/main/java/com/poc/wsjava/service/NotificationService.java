package com.poc.wsjava.service;

import com.poc.wsjava.dto.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendGlobalNotification() {
        ResponseMessage message = new ResponseMessage("Global Notification");
        messagingTemplate.convertAndSend("/topic/global-notifications", message);
    }

    public void sendCustomNotification(final String userId) {
        ResponseMessage message = new ResponseMessage("Custom Notification");
        messagingTemplate.convertAndSendToUser(userId,"/topic/custom-notifications", message);
    }
}
