package com.poc.wsjava.kafka.listeners;

import com.poc.wsjava.kafka.messages.EventMessage;
import com.poc.wsjava.kafka.messages.PayloadMessage;
import com.poc.wsjava.service.AgentSenderService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;
import java.util.stream.StreamSupport;

@Component
public class KafkaListeners {
    private final Logger logger = LoggerFactory.getLogger(KafkaListeners.class);
    @Autowired
    private AgentSenderService agentService;
    private final CountDownLatch latch = new CountDownLatch(10);
    ;

    @KafkaListener(topics = "events", groupId = "tpd-loggers")
    void eventListener(@Payload EventMessage data) {
        logger.info("Event received: [ " + data.getEventDetail() + " ]..");
        agentService.deliverAgentEvents(data);
    }

    @KafkaListener(topics = "notifications", groupId = "tpd-loggers")
    void notificationListener(@Payload EventMessage data) {
        logger.info("Notification received: [ " + data + " ]..");
    }

    @KafkaListener(
            id = "c_p_1",
            topicPartitions =
                    {
                            @TopicPartition(topic = "payload-event", partitions = {"0"})
                                    // ,partitionOffsets = @PartitionOffset(partition = "*", initialOffset = "100")),
                            // @TopicPartition(topic = "events", partitions = "0", partitionOffsets = @PartitionOffset(partition = "*", initialOffset = "100"))
                    },
            // topics = "payload-event",
            clientIdPrefix = "listener-json",
            containerFactory = "kafkaListenerContainerFactory",
            groupId = "tpd-loggers")
    void payloadObjectListener1(
            ConsumerRecord<String, String> cr, @Payload EventMessage data
    ) {
        logger.info("Logger 1 [Json] received key {}: | Partition: {} | Type [{}] | Payload: {} ", cr.key(), cr.partition(),
                typeIdHeader(cr.headers()), data);
        latch.countDown();
        agentService.selfQueueDeliveryEventsByListenerId(data, cr.key(), cr.partition(),"C_P_1");

    }

    @KafkaListener(
            id = "c_p_2",
            topicPartitions =
                    {       // No se puede tener dos veces la misma configuracion de partitionOffsets para le mismo topico
                            @TopicPartition(topic = "payload-event", partitions = {/*"0",*/ "1"})
                    },
            clientIdPrefix = "listener-json",
            containerFactory = "kafkaListenerContainerFactory",
            groupId = "tpd-loggers")
    void payloadEventsListener2(
            ConsumerRecord<String, String> cr, @Payload EventMessage data
    ) {
        logger.info("Logger 2 [Json] received key {}: | Partition: {} | Type [{}] | Payload: {} ", cr.key(), cr.partition(),
                typeIdHeader(cr.headers()), data);
        latch.countDown();
        agentService.selfQueueDeliveryEventsByListenerId(data, cr.key(), cr.partition(),"C_P_2");

    }

    @KafkaListener(
            id = "c_p_3",
            topicPartitions =
                    {       // No se puede tener dos veces la misma configuracion de partitionOffsets para le mismo topico
                            @TopicPartition(topic = "payload-event", partitions = {"2"})
                    },
            // topics = "payload-event",
            clientIdPrefix = "listener-json",
            containerFactory = "kafkaListenerContainerFactory",
            groupId = "tpd-loggers")
    void payloadEventsListener3(
            ConsumerRecord<String, String> cr, @Payload EventMessage data
    ) {
        logger.info("Logger 3 [Json] received key {}: | Partition: {} | Type [{}] | Payload: {} ", cr.key(), cr.partition(),
                typeIdHeader(cr.headers()), data);
        latch.countDown();
        agentService.selfQueueDeliveryEventsByListenerId(data, cr.key(), cr.partition(),"C_P_3");

    }

    /*
    @KafkaListener(
            topics = "payload-event",
            clientIdPrefix = "Pfx-String",
            containerFactory = "kafkaListenerStringContainerFactory",
            groupId = "tpd-loggers"
    )
    public void payloadStringListener(
            ConsumerRecord<String, String> cr, @Payload String data
    ) {
        logger.info("Logger 2 [String] received key {}: Type [{}] | Payload: {} | Record: {} | Partition: {}", cr.key(),
                typeIdHeader(cr.headers()), data, cr.toString(), cr.partition());
        latch.countDown();
    }

    @KafkaListener(
            topics = "payload-event",
            clientIdPrefix = "Pfx-Bytearray",
            containerFactory = "kafkaListenerByteArrayContainerFactory",
            groupId = "tpd-loggers"
    )
    public void payloadAsByteArrayListener(
            ConsumerRecord<String, byte[]> cr, @Payload byte[] payload
    ) {
        logger.info("Logger 3 [ByteArray] received key {}: Type [{}] | Payload: {} | Record: {} | Partition: {}", cr.key(),
                typeIdHeader(cr.headers()), payload, cr.toString(), cr.partition());
        latch.countDown();
    }
    */

    private static String typeIdHeader(Headers headers) {
        return StreamSupport.stream(headers.spliterator(), false)
                .filter(header -> header.key().equals("__TypeId__"))
                .findFirst().map(header -> new String(header.value())).orElse("N/A");
    }
}
