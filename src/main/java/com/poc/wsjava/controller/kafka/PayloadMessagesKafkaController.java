package com.poc.wsjava.controller.kafka;

import org.apache.kafka.common.utils.Utils;
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



@RestController
@RequestMapping("/v1/kafka/payloads")
public class PayloadMessagesKafkaController {

    private static final Logger logger = LoggerFactory.getLogger(PayloadMessagesKafkaController.class);
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${tpd.topic-name}")
    private String topicName;

    @Value("${tpd.partitions}")
    private Integer partitions;

    @Value("${tpd.messages-per-request}")
    private int messagesPerRequest;

    private CountDownLatch latch;


    /**
     * Notes:
     *          Se pueden enviar eventos directamentes a la particion que se desee, y con el key que deseemos,
     *                  ->  send(String topic, Integer partition, K key, @Nullable V data)
     *
     *          - No se asigna directamente una particion sino que decide kafka con el round robin, la diferencia es que con la key
     *             Siempre que un mensaje caiga con la misma key kafka lo enviara a la particion designada sin necesidad de especificarlo
     *
     *          - otra opcion es decirle a kafka que particion escribira por ejemplo 1 0 2 y ahi siempre buscar esos key.
     */

    @PostMapping()
    public void produceAnEvent(@RequestBody PayloadMessage message) {
        //->  send(String topic, Integer partition, K key, @Nullable V data)
        kafkaTemplate.send("events", message);
    }


    /**
     *   Mover esta logica de MURMUR a donde se crean las conecciones de WS para validar si el Partition
     *   que se resuelve es parte del consumo de este nodo
     * */

    @PostMapping("/test")
    public String producePayloads(@RequestBody List<EventMessage> users) throws Exception {
        latch = new CountDownLatch(messagesPerRequest);
        users.forEach( user -> {

            /** Replicar Hash key con MURMUR kafka Utils -> Obtener la particion en base al key id del usuario. **/
            //int partitionSelected =  Utils.toPositive(Utils.murmur2(user.getIdUser().getBytes())) % partitions;
            /** Imprimir Hash y particion obtenida para comparar con el del evento enviado */
            //logger.info(String.format("User ID [ '%s' ], partition [ '%d' ]", user.getIdUser(), partitionSelected));

              this.kafkaTemplate.send(
                    topicName,
                    // aca podria ir la particion ej : 0
                    user.getIdUser(),
                    user
            );
        });
        latch.await(20, TimeUnit.SECONDS);
        logger.info("All messages received");
        return "All messages received!! take a ☕️";
    }

}
