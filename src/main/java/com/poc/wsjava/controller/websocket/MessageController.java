package com.poc.wsjava.controller.websocket;

import com.poc.wsjava.dto.Message;
import com.poc.wsjava.dto.ResponseMessage;
import com.poc.wsjava.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.security.Principal;

@Controller
public class MessageController {

    @Autowired
    private NotificationService notificationService;

    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public ResponseMessage getMessage(final Message message) throws InterruptedException {
        Thread.sleep(1000);
        notificationService.sendGlobalNotification();
        return new ResponseMessage(HtmlUtils.htmlEscape(message.getMessageContent()));
    }

    @MessageMapping("/custom-message")
    @SendToUser("/topic/custom-messages")
    public ResponseMessage getCustomMessage(final Message message, @Header(name = "userId") String userId, // Se puede obtener el header
                                            final Principal principal) throws InterruptedException {
        Thread.sleep(1000);
        notificationService.sendCustomNotification(principal.getName());
        return new ResponseMessage("Sending Custom message to specific device [ " + principal.getName() + " ]: " + HtmlUtils.htmlEscape(message.getMessageContent()));
    }
}
