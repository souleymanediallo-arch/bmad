package com.example.demo.controller;

import com.example.demo.dto.QueryRequest;
import com.example.demo.dto.RagResponse;
import com.example.demo.service.GenerationService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/query")
public class QueryController {

    private final GenerationService generationService;

    public QueryController(GenerationService generationService) {
        this.generationService = generationService;
    }

    @PostMapping
    public RagResponse query(@RequestBody QueryRequest request) {
        String sessionId = request.getSessionId() != null ? request.getSessionId() : UUID.randomUUID().toString();
        return generationService.generateAnswer(request.getQuery(), sessionId);
    }
}
