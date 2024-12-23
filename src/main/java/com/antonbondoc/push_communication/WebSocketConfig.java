package com.antonbondoc.push_communication;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    /*
    * Reference:
    * - https://medium.com/@ropelife/real-time-communication-with-websocket-in-spring-boot-using-webflux-5d9fbb36a0ab
    * */

    private final CustomWebSocketHandler webSocketHandler;

    public WebSocketConfig(CustomWebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // You need to insert your own custom web socket handler here
        registry.addHandler(webSocketHandler, "/web-socket")
                .setAllowedOrigins("*");
    }

}
