package com.poc.wsjava.config;


import com.poc.wsjava.kafka.utils.PartitionHashHandler;
import com.sun.security.auth.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.*;

public class UserHandshakeHandler extends DefaultHandshakeHandler {
    private final Logger logger = LoggerFactory.getLogger(UserHandshakeHandler.class);
    private final PartitionHashHandler handler = new PartitionHashHandler();

    @Override
    protected Principal determineUser(ServerHttpRequest request,
                                      WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {

        String randomId = UUID.randomUUID().toString();
        logger.info("New Ws User ID '{}'... ", randomId);
        int partitionSelected = handler.getSelectedPartitionByMurmur2Hash(randomId);
        boolean userInPartitionList = handler.checkIfUserIsInPartitionsListened(logger, partitionSelected);
        logger.info(String.format("User ID [ '%s' ], partition [ '%d' ], is Listed on this MS: [ '%s' ]", randomId, partitionSelected, userInPartitionList));

        return new UserPrincipal(randomId);
    }

}
