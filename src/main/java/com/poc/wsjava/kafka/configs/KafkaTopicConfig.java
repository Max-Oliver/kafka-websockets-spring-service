package com.poc.wsjava.kafka.configs;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic eventTopic(){
        Map<String, String> configuration = new HashMap<>();
        //configuration.put(TopicConfig.CLEANUP_POLICY_CONFIG, TopicConfig.CLEANUP_POLICY_DELETE); // delete (Borra el mensaje)
        configuration.put(TopicConfig.CLEANUP_POLICY_CONFIG, TopicConfig.CLEANUP_POLICY_COMPACT);  // Compact (Mantiene el mas actualizado)
        configuration.put(TopicConfig.RETENTION_MS_CONFIG, "86400000");  // Tiempo de retencion de milissegs de mensajes - por defecto (-1) no se borran
        configuration.put(TopicConfig.SEGMENT_BYTES_CONFIG, "1073741824"); // Tamaño maximo de bytes del segmento - por defecto 1gb
        configuration.put(TopicConfig.MAX_MESSAGE_BYTES_CONFIG, "10003344"); // Tamaño maximo de cada mensaje - por defecto 1MB

        return TopicBuilder.name("events")
                .partitions(2)
                .replicas(2)
                .configs(configuration)
                .build();
    }

    @Bean
    public NewTopic notificationTopic(){

        Map<String, String> configuration = new HashMap<>();
        //configuration.put(TopicConfig.CLEANUP_POLICY_CONFIG, TopicConfig.CLEANUP_POLICY_DELETE); // delete (Borra el mensaje)
        configuration.put(TopicConfig.CLEANUP_POLICY_CONFIG, TopicConfig.CLEANUP_POLICY_COMPACT);  // Compact (Mantiene el mas actualizado)
        configuration.put(TopicConfig.RETENTION_MS_CONFIG, "86400000");  // Tiempo de retencion de milissegs de mensajes - por defecto (-1) no se borran
        configuration.put(TopicConfig.SEGMENT_BYTES_CONFIG, "1073741824"); // Tamaño maximo de bytes del segmento - por defecto 1gb
        configuration.put(TopicConfig.MAX_MESSAGE_BYTES_CONFIG, "10003344"); // Tamaño maximo de cada mensaje - por defecto 1MB

        return TopicBuilder.name("notifications")
                .partitions(2)
                .replicas(2)
                .configs(configuration)
                .build();

    }
}
