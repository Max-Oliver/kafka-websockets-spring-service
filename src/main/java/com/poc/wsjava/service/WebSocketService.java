package com.poc.wsjava.service;

import com.poc.wsjava.dto.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {

    private final SimpMessagingTemplate msjTemplate;
    private final NotificationService notificationService;

    @Autowired
    public WebSocketService(SimpMessagingTemplate msjTemplate, NotificationService notificationService){
        this.msjTemplate = msjTemplate;
        this.notificationService = notificationService;
    }
    public void notifyFrontChanges(final String message){
        ResponseMessage response = new ResponseMessage(message);
        notificationService.sendGlobalNotification();

        msjTemplate.convertAndSend("/topic/messages", response);
    }

    public void notifyFrontForCustomChanges(final String id, final String message){
        ResponseMessage response = new ResponseMessage(message);
        notificationService.sendCustomNotification(id);

        msjTemplate.convertAndSendToUser(id,"/topic/custom-messages", response);
    }
}
