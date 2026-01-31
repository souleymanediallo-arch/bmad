package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

@Service
public class ModelWarmupService implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger logger = LoggerFactory.getLogger(ModelWarmupService.class);
    private final OllamaChatClient chatClient;

    public ModelWarmupService(OllamaChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        logger.info("Starting model warm-up process...");
        try {
            String response = chatClient.call("Hello, are you ready?");
            logger.info("Model warm-up successful. Response: {}", response);
        } catch (Exception e) {
            logger.error("Model warm-up failed: {}", e.getMessage());
        }
    }
}
