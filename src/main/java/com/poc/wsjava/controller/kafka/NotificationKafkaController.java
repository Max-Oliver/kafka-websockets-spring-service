package com.poc.wsjava.controller.kafka;

import com.poc.wsjava.dto.KafkaMessageRequest;
import com.poc.wsjava.kafka.messages.EventMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/kafka/notifications")
public class NotificationKafkaController {
    @Autowired
    private  KafkaTemplate<String, Object> kafkaTemplate;

    @PostMapping()
    public void produceAnNotification(@RequestBody EventMessage event) {
        kafkaTemplate.send("notifications", event);
    }

}
