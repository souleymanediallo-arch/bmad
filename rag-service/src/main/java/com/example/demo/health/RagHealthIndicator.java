package com.example.demo.health;

import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("rag")
public class RagHealthIndicator implements HealthIndicator {

    private final OllamaChatModel chatClient;

    public RagHealthIndicator(OllamaChatModel chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public Health health() {
        Map<String, Object> details = new HashMap<>();
        try {
            // Simple check for Ollama connectivity
            chatClient.call(new Prompt("health check"));
            details.put("ollama", "UP");
            return Health.up().withDetails(details).build();
        } catch (Exception e) {
            details.put("ollama", "DOWN");
            details.put("error", e.getMessage());
            return Health.down().withDetails(details).build();
        }
    }
}
