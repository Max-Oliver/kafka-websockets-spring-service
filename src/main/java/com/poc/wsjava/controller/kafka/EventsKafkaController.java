package com.poc.wsjava.controller.kafka;

import com.poc.wsjava.dto.KafkaMessageRequest;
import com.poc.wsjava.kafka.messages.EventMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/v1/kafka/events")
public class EventsKafkaController {
    @Autowired
    private KafkaTemplate<String, EventMessage> kafkaTemplate;

    @PostMapping()
    public void produceAnEvent(@RequestBody EventMessage event) {
        kafkaTemplate.send("events", event);
    }

    @PostMapping("/test/{user_id}")
    public void produceEvents(@PathVariable(name = "user_id") String userId){
        EventMessage event = new EventMessage();
        event.setIdUser(userId);
        event.setUsername("Username By Postman");
        event.setTypeEvent("Events");
        for (int i = 0; i < 10; i++) {
            event.setEventDetail(String.format("Event n° [ "+ i +" ] by User [ %s ]", event.getIdUser()));
            kafkaTemplate.send("events", event);
        }
    }

}
