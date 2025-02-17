package com.project.trading212.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.project.trading212.backend.repository.CryptoWebSocketRepository;
import com.project.trading212.backend.service.CryptoWebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import java.net.URI;

@Configuration
@RequiredArgsConstructor
@EnableWebSocket
@Slf4j
public class KrakenWSConfig implements WebSocketConfigurer {
    private final CryptoWebSocketService cryptoService;
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(krakenWSHandler(), "ws/crypto").setAllowedOrigins("*");

    }
    @Bean
    public ObjectMapper objectMapper() {
       return JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();
    }
        @Bean
        public WebSocketHandler krakenWSHandler(){
            return new KrakenWSHandler(objectMapper(),cryptoService);
        }

        @Bean
        public WebSocketClient krakenWSClient(){
        return new StandardWebSocketClient();
        }

        @EventListener(ApplicationReadyEvent.class)
    public void connectWebSocket(){
        try{
            WebSocketClient client = krakenWSClient();
            client.execute(
                    krakenWSHandler(),
                    new WebSocketHttpHeaders(),
                    URI.create("wss://ws.kraken.com/v2")
            );
        } catch (Exception e) {
            log.error("Failed to connect to WebSocket: {}",e.getMessage());
        }
        }
}
