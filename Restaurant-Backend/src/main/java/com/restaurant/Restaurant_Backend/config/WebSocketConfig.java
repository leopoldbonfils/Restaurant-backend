package com.restaurant.Restaurant_Backend.config;

package com.restaurant.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * STOMP over WebSocket configuration.
 *
 * Client connection flow:
 *   1. Connect to  ws://localhost:8080/ws  (SockJS fallback available)
 *   2. Subscribe to topic for updates:
 *      - /topic/orders          – all active orders (kitchen board)
 *      - /topic/orders/{table}  – updates for one table (customer tracking)
 *   3. Send messages to /app/... if needed (currently server-push only)
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // In-memory broker for topics the server pushes to clients
        registry.enableSimpleBroker("/topic");
        // Prefix for @MessageMapping methods (client → server)
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")   // tighten in production
                .withSockJS();                   // SockJS fallback for older browsers
    }
}
