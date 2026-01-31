package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "query_logs")
public class QueryLog {

    @Id
    @Column(name = "correlation_id")
    private String correlationId;

    @Column(columnDefinition = "TEXT")
    private String originalQuery;

    @Column(columnDefinition = "TEXT")
    private String answer;

    private Double confidenceScore;

    private Long processingTimeMs;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    private Integer feedback; // 1 for thumbs up, -1 for thumbs down

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getCorrelationId() { return correlationId; }
    public void setCorrelationId(String correlationId) { this.correlationId = correlationId; }
    public String getOriginalQuery() { return originalQuery; }
    public void setOriginalQuery(String originalQuery) { this.originalQuery = originalQuery; }
    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }
    public Double getConfidenceScore() { return confidenceScore; }
    public void setConfidenceScore(Double confidenceScore) { this.confidenceScore = confidenceScore; }
    public Long getProcessingTimeMs() { return processingTimeMs; }
    public void setProcessingTimeMs(Long processingTimeMs) { this.processingTimeMs = processingTimeMs; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public Integer getFeedback() { return feedback; }
    public void setFeedback(Integer feedback) { this.feedback = feedback; }
}
