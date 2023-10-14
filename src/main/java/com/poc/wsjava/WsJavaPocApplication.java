package com.poc.wsjava;

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

}
