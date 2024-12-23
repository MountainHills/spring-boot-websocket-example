package com.antonbondoc.push_communication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
public class CustomWebSocketHandler extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomWebSocketHandler.class);

    private final CopyOnWriteArraySet<WebSocketSession> sessions = new CopyOnWriteArraySet<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("A connection has been established");

        sessions.add(session);

        // Get the user that joined the websocket server
        String sessionUser = getSessionUser(session);

        // Inform all the existing sessions that a user has joined the websocket server
        TextMessage textMessage = new TextMessage("Welcome! " + sessionUser + " has connected to the websocket server.");
        pushMessageToSessions("Welcome! " + sessionUser + " has connected to the websocket server.");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String sessionUser = getSessionUser(session);
        logger.info("Web socket server has received payload information from {}", sessionUser);

        String sessionMessage = message.getPayload();

        // Tell all the existing session what the session user said
        pushMessageToSessions(sessionUser + " says: " + sessionMessage);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String sessionUser = getSessionUser(session);
        sessions.remove(session);

        logger.info("Disconnecting {} from websocket server.", sessionUser);

        // Inform all existing sessions that user has disconnected from the server
        pushMessageToSessions(sessionUser + " has disconnected");
    }

    private static String getSessionUser(WebSocketSession session) {
        int remotePort = session.getRemoteAddress().getPort();
        return "User#" + remotePort;
    }

    public void pushMessage(String message) throws IOException {
        pushMessageToSessions("Server sent: " + message);
    }

    private void pushMessageToSessions(String message) throws IOException {
        TextMessage textMessage = new TextMessage(message);
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                session.sendMessage(textMessage);
            }
        }
    }
}
