package com.poc.wsjava.kafka.utils;

import org.apache.kafka.common.utils.Utils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class PartitionHashHandler {

    /**
     * @Note: Esta cantidad de particiones es de todo el topico
     */
    //@Value("${ms-consumer-group.partitions}")
    //private List<Integer> partitions;


    @Value("${topic.create.partition:3}")
    private final Integer partition = 3;

    /**
     * Esta son las particiones que este nodo escucha
     */
    private final List<Integer> listenedPartitions = new ArrayList<>(List.of(0, 1));


    public Integer getSelectedPartitionByMurmur2Hash(String id) {
    /** Replicar Hash key con MURMUR kafka Utils -> Obtener la particion en base al key id del usuario. **/
        return Utils.toPositive(Utils.murmur2(id.getBytes())) % partition;
    }

    public boolean checkIfUserIsInPartitionsListened(Logger logger, Integer partitionSelected) {
        AtomicBoolean flag = new AtomicBoolean(false);
        /** Validamos si el usuario existe en las particiones que escuchamos en este nodo */
        listenedPartitions.forEach(it -> {
            logger.info("it: [ '{}' ], partitionSelected: [ '{}' ]", it, partitionSelected);
            if (it.compareTo(partitionSelected) == 0) {
                flag.set(true);
            }
        });

        return flag.get();
    }

}
