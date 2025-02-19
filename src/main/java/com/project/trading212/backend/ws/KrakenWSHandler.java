package com.project.trading212.backend.ws;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.trading212.backend.model.dto.CryptoWebSocketDto;
import com.project.trading212.backend.service.CryptoWebSocketService;
import com.project.trading212.backend.util.CryptoInitializerClass;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class KrakenWSHandler extends TextWebSocketHandler {
    private static final String KRAKEN_WS_URL = "wss://ws.kraken.com/v2";
    private static final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper mapper;
    private final Map<String, CryptoWebSocketDto> pricemap = new ConcurrentHashMap<>();
    private final CryptoWebSocketService cryptoService;
    private WebSocketSession currentSession;

    @PostConstruct
    public void init() {
        cryptoService.initializeCryptoEntries();
    }

    @PreDestroy
    public void cleanup() {
        removeAllSessions();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        this.currentSession = session;
        log.info("WebSocket connection established");
        sessions.put(session.getId(), session);
        subscribeToTicker(session);
    }

    private void subscribeToTicker(WebSocketSession session) throws IOException {
        String subscribeMessage = """
                 {
                              "method":"subscribe",
                              "params": {
                              "channel": "ticker",
                              "snapshot": true,
                              "symbol":
                               ["BTC/USD", "ETH/USD", "SOL/USD", "AAVE/USD", "LTC/USD", "FARM/USD", "QNT/USD","YFI/USD","PAXG/USD", "WBTC/USD", "WBTC/USD", "MSOL/USD","XMR/USD","COMP/USD","ZEC/USD","GNO/USD","MKR/USD","TAO/USD","BCH/USD","TBTC/USD"]
                              }
                }""";
        session.sendMessage(new TextMessage(subscribeMessage));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            JsonNode jsonNode = mapper.readTree(message.getPayload());
            String payload = message.getPayload();
            log.debug("Received message: {}", payload);

            if (jsonNode.has("error")) {
                log.error("Kraken error: {}", jsonNode.get("error").asText());
                return;
            }
            if (jsonNode.has("channel") && "ticker".equals(jsonNode.get("channel").asText())) {
                JsonNode tickerData = jsonNode.path("data").path(0);
                log.debug("Received ticker data: {}", tickerData);
                handlePriceUpdate(tickerData);

            }
        } catch (JsonProcessingException e) {
            log.error("Error processing message: {}", e.getMessage());
        }
    }

    private void handlePriceUpdate(JsonNode data) {

        String symbol = data.get("symbol").asText();
        BigDecimal price = new BigDecimal(data.get("ask").asText());
        String name = CryptoInitializerClass.getFullName(symbol);
        LocalDateTime updateTime = LocalDateTime.now();
        CryptoWebSocketDto cryptoPrice = new CryptoWebSocketDto(name, symbol, price);
        cryptoService.handlePriceUpdate(cryptoPrice);
        try {
            String jsonMessage = mapper.writeValueAsString(cryptoPrice);
            broadcast(jsonMessage);
        } catch (JsonProcessingException e) {
            log.error("Error broadcasting price update: {}", e.getMessage());
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        log.error("WebSocket transport error: {}", exception.getMessage());
        scheduleReconnect();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {

        log.warn("WebSocket connection closed: {}", status);
        sessions.remove(session.getId());
        scheduleReconnect();
    }

    public void broadcast(String message) {
        TextMessage textMessage = new TextMessage(message);
        sessions.forEach((id, session) -> {
            try {
                if (session.isOpen()) {
                    session.sendMessage(textMessage);
                }
            } catch (IOException e) {
                sessions.remove(id);
            }
        });
    }

    @Scheduled(fixedDelay = 5000)
    private void scheduleReconnect() {
        try {
            if (currentSession == null || !currentSession.isOpen()) {
                log.info("Attempting to reconnect...");
            }
        } catch (Exception e) {
            log.error("Reconnection attempt failed: {}", e.getMessage());

        }
    }

    public void removeAllSessions() {
        sessions.forEach((sessionId, session) -> {
            sessions.remove(session.getId());
        });
        sessions.clear();
        log.info("All sessions removed");
    }
}
