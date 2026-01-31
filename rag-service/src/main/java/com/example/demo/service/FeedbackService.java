package com.example.demo.service;

import com.example.demo.dto.FeedbackRequest;
import com.example.demo.repository.QueryLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FeedbackService {

    private static final Logger logger = LoggerFactory.getLogger(FeedbackService.class);
    private final QueryLogRepository repository;

    public FeedbackService(QueryLogRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void submitFeedback(FeedbackRequest request) {
        logger.info("Submitting feedback for correlation ID: {}", request.correlationId());
        repository.findById(request.correlationId()).ifPresentOrElse(log -> {
            log.setFeedback(request.rating());
            repository.save(log);
            logger.info("Successfully updated feedback for correlation ID: {}", request.correlationId());
        }, () -> {
            logger.warn("Correlation ID not found: {}", request.correlationId());
            throw new IllegalArgumentException("Correlation ID not found: " + request.correlationId());
        });
    }
}
