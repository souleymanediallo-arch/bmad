package com.example.demo.dto;

public record FeedbackRequest(
    String correlationId,
    Integer rating // 1 for thumbs up, -1 for thumbs down
) {}
