package com.antonbondoc.push_communication;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class WebSocketController {

    private final CustomWebSocketHandler webSocketHandler;

    public WebSocketController(CustomWebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @GetMapping("/push-message")
    public String pushMessage(@RequestParam String message) {
        try {
            webSocketHandler.pushMessage(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "Messaged pushed to clients: " + message;
    }
}
