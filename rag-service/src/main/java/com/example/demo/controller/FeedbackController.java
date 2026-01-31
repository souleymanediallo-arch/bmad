package com.example.demo.controller;

import com.example.demo.dto.FeedbackRequest;
import com.example.demo.service.FeedbackService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    public ResponseEntity<String> submitFeedback(@RequestBody FeedbackRequest request) {
        try {
            feedbackService.submitFeedback(request);
            return ResponseEntity.ok("Feedback submitted successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
