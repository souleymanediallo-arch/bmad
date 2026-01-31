package com.example.demo.health;

import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("rag")
public class RagHealthIndicator implements HealthIndicator {

    private final OllamaChatClient chatClient;

    public RagHealthIndicator(OllamaChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public Health health() {
        Map<String, Object> details = new HashMap<>();
        try {
            // Simple check for Ollama connectivity
            chatClient.call("health check");
            details.put("ollama", "UP");
            return Health.up().withDetails(details).build();
        } catch (Exception e) {
            details.put("ollama", "DOWN");
            details.put("error", e.getMessage());
            return Health.down().withDetails(details).build();
        }
    }
}
