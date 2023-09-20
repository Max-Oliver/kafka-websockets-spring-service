package com.poc.wsjava.config;


import com.sun.security.auth.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import java.security.Principal;
import java.util.Map;
import java.util.UUID;

public class UserHandshakeHandler extends DefaultHandshakeHandler {
    private final Logger logger = LoggerFactory.getLogger(UserHandshakeHandler.class);

    @Override
    protected Principal determineUser(ServerHttpRequest request,
                                      WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {

        // ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
        // HttpServletRequest httpServletRequest = servletRequest.getServletRequest();
        // String userId = httpServletRequest.getQueryString().split("=")[2];

        // if(userId.equals("userId-1")) {
        //     logger.info("Valid User - User ID '{}': ", userId);
        // }

        String randomId = UUID.randomUUID().toString();
        logger.info("New Ws User ID '{}'... ", randomId);
        return new UserPrincipal(randomId);
    }

}
