package com.project.trading212.backend.config;

import com.corundumstudio.socketio.SocketIOServer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.trading212.backend.service.CryptoWebSocketService;
import com.project.trading212.backend.ws.KrakenWSHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;


@Configuration
@RequiredArgsConstructor
@EnableWebSocket
@Slf4j
public class KrakenWSConfig implements WebSocketConfigurer {
    private final CryptoWebSocketService cryptoService;
    private final ObjectMapper objectMapper;
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(krakenWSHandler(), "ws/crypto").setAllowedOrigins("*");

    }
    @Bean
    public WebSocketHandler krakenWSHandler() {
        return new KrakenWSHandler(objectMapper, cryptoService);
    }

    @Bean
    public WebSocketClient krakenWSClient() {
        return new StandardWebSocketClient();
    }
}
