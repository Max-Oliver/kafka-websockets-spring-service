package com.poc.wsjava.controller.kafka;

import com.poc.wsjava.kafka.messages.EventMessage;
import com.poc.wsjava.kafka.messages.PayloadMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/v1/kafka/payloads")
public class PayloadMessagesKafkaController {

    private static final Logger logger =
            LoggerFactory.getLogger(PayloadMessagesKafkaController.class);
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${tpd.topic-name}")
    private String topicName;

    @Value("${tpd.messages-per-request}")
    private int messagesPerRequest;

    private CountDownLatch latch;

    @PostMapping()
    public void produceAnEvent(@RequestBody PayloadMessage message) {
        //->  send(String topic, Integer partition, K key, @Nullable V data)
        kafkaTemplate.send("events", message);
    }

    @PostMapping("/test")
    public String producePayloads(@RequestBody List<EventMessage> users) throws Exception {
        latch = new CountDownLatch(messagesPerRequest);
        users.forEach( user -> {
            logger.info("User ID [ '{}' ]-> send.", user.getIdUser());

            // No se asigna directamente una particion sino que decide kafka con el round robin, la diferencia es que con la key
            // Siempre que un mensaje caiga con la misma key kafka lo enviara a la particion designada sin necesidad de especificarlo

            // otra opcion es decirle a kafka que particion escribira por ejemplo 1 0 2 y ahi siempre buscar esos key.
            this.kafkaTemplate.send(
                    topicName,
                    // aca podria ir la particion ej : 0
                    user.getIdUser(),
                    user
            );
        });
        /*
        IntStream.range(0, 5)
                .forEach(idx ->
         // ->  send(String topic, Integer partition, K key, @Nullable V data)
         this.kafkaTemplate.send(
                 topicName,
                 0,
                 "user_id_1",
                 new PayloadMessage("Name: Carlos Alberto, Pais: Colombia", idx)
         )
                );
        IntStream.range(5, 10)
                .forEach(idx ->
                        // ->  send(String topic, Integer partition, K key, @Nullable V data)
                        this.kafkaTemplate.send(
                                topicName,
                                1,
                                "user_id_3",
                                new PayloadMessage("Name: Miguel Otero, Pais: Brasil", idx)
                        )
                );

        IntStream.range(10, 15)
                .forEach(idx ->
                        // ->  send(String topic, Integer partition, K key, @Nullable V data)
                        this.kafkaTemplate.send(
                                topicName,
                                1,
                                "user_id_2",
                                new PayloadMessage("Name: Miguel Otero, Pais: Brasil", idx)
                        )
                );
         */
        latch.await(20, TimeUnit.SECONDS);
        logger.info("All messages received");
        return "All messages received!! take a ☕️";
    }

}
