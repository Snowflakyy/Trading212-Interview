package com.project.trading212.backend.ws;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;

import java.net.URI;

@Component
@RequiredArgsConstructor
@Slf4j
public class KrakenWSConnector {
    private final WebSocketClient krakenWSClient;
    private final WebSocketHandler krakenWsHandler;

    @EventListener(ApplicationReadyEvent.class)
    public void connectWebSocket() {
        try {

            krakenWSClient.execute(krakenWsHandler, new WebSocketHttpHeaders(), URI.create("wss://ws.kraken.com/v2"));
        } catch (Exception e) {
            log.error("Failed to connect to WebSocket: {}", e.getMessage());
        }
    }

}
