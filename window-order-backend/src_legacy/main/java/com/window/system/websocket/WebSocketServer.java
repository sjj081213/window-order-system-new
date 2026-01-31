package com.window.system.websocket;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
@Component
@ServerEndpoint("/ws/{userId}/{role}")
public class WebSocketServer {

    // Store all active WebSocket sessions
    private static final CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<>();
    
    private Session session;
    private String userId;
    private String role;

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId, @PathParam("role") String role) {
        this.session = session;
        this.userId = userId;
        this.role = role;
        
        webSocketSet.add(this);
        log.info("WebSocket connected: userId={}, role={}, current connections={}", userId, role, webSocketSet.size());
    }

    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        log.info("WebSocket disconnected: userId={}, current connections={}", userId, webSocketSet.size());
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("WebSocket error", error);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // Handle client messages if needed
    }

    // Push message to specific user
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    // Broadcast to all admins
    public static void sendToAdmins(String message) {
        for (WebSocketServer item : webSocketSet) {
            if ("ADMIN".equals(item.role)) {
                try {
                    item.sendMessage(message);
                } catch (IOException e) {
                    log.error("Failed to send message to admin: " + item.userId, e);
                }
            }
        }
    }
}
