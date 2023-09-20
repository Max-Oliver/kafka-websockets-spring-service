package com.poc.wsjava.service;

import com.poc.wsjava.kafka.messages.EventMessage;
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

    private String validateWebSocketOpenedByAgents(String principal) {
        return principal;
    }
}
