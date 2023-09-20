package com.poc.wsjava.controller.http;

import com.poc.wsjava.dto.Message;
import com.poc.wsjava.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebSocketController {

    @Autowired
    private WebSocketService service;

    @PostMapping("/send-message")
    public void sendMessage(@RequestBody final Message message){
        service.notifyFrontChanges(message.getMessageContent());
    }

    @PostMapping("/send-custom-message/{id}")
    public void sendCustomMessage(@PathVariable final String id,
                                  @RequestBody final Message message){
        service.notifyFrontForCustomChanges(id,message.getMessageContent());
    }
}
