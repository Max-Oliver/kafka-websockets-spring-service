package com.poc.wsjava.service;

import com.poc.wsjava.kafka.messages.EventMessage;
import com.poc.wsjava.kafka.messages.PayloadMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgentSenderService {
    private final Logger logger = LoggerFactory.getLogger(AgentSenderService.class);
    @Autowired
    private WebSocketService wsService;

    public void deliverAgentEvents(EventMessage event) {
        String wsUserID = validateWebSocketOpenedByAgents(event.getIdUser());
        if (wsUserID.equals(event.getIdUser())) {
            wsService.notifyFrontForCustomChanges(event.getIdUser(), event.getEventDetail());
        } else {
            logger.warn("WS Not found with the User ID '{}' and Event IDUser: '{}'", wsUserID, event.getIdUser());
        }
    }

    public void selfQueueDeliveryEventsByMs(EventMessage event) {
        String wsUserID = validateWebSocketOpenedByAgents(event.getIdUser());
        if (wsUserID.equals(event.getIdUser())) {
            wsService.notifyFrontForCustomChanges(event.getIdUser(), event.getEventDetail());
        } else {
            logger.warn("WS Not found with the User ID '{}' and Event IDUser: '{}'", wsUserID, event.getIdUser());
        }
    }

    public void selfQueueDeliveryPayloadMessage(EventMessage payload, String listenerMessage) {
        //String wsUserID = validateWebSocketOpenedByAgents(event.getIdUser());
        //if (wsUserID.equals(event.getIdUser())) {
            //wsService.notifyFrontForCustomChanges(event.getIdUser(), event.getEventDetail());
        logger.info(" '{}' Published on WS -> [ '{}' ]..", listenerMessage, payload);
        String message = String.format("Message from Listener [ %s ] with the payload [ %s ]..",listenerMessage, payload.getEventDetail());
        wsService.notifyFrontChanges(message);
        //} else {
        //    logger.warn("WS Not found with the User ID '{}' and Event IDUser: '{}'", wsUserID, event.getIdUser());
        //}
    }

    public void selfQueueDeliveryEventsByListenerId(EventMessage event,String key, int partition, String listenerID) {
        String wsUserID = validateWebSocketOpenedByAgents(event.getIdUser());
        if (wsUserID.equals(event.getIdUser())) {
            String message = String.format("Message Key [ %s ] Listener [ %s ] in partition [ %s ] with the payload [ %s ]..", key, listenerID, partition, event.toString());
            wsService.notifyFrontForCustomChanges(event.getIdUser(), message);
            logger.info(" '{}' Published on WS -> [ '{}' ]..", listenerID, event.toString());
        } else {
            logger.warn("WS Not found with the User ID '{}' and Event IDUser: '{}'", wsUserID, event.getIdUser());
        }
    }

    private String validateWebSocketOpenedByAgents(String principal) {
        return principal;
    }
}
