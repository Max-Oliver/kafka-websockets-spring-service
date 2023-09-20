package com.poc.wsjava;

import com.poc.wsjava.kafka.messages.EventMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootApplication
public class WsJavaPocApplication {

	public static void main(String[] args) {
		SpringApplication.run(WsJavaPocApplication.class, args);
	}


	@Bean
	CommandLineRunner commandLineRunner(@Autowired KafkaTemplate<String, EventMessage> kafkaTemplate) {
		return args -> {
		//	EventMessage event = new EventMessage();
		//	event.setIdUser("User ID");
		//	event.setUsername("Username");
		//		event.setTypeEvent("Events");
		//		for (int i = 0; i < 10; i++) {
		//			event.setEventDetail(String.format("Event N° [ "+ i +" ] by user [ %s ]", event.getIdUser()));
		//			kafkaTemplate.send("events", event);
		//		}
		};
	}

}
