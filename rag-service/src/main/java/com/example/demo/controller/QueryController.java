package com.example.demo.controller;

import com.example.demo.dto.RagResponse;
import com.example.demo.service.GenerationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/query")
public class QueryController {

    private final GenerationService generationService;

    public QueryController(GenerationService generationService) {
        this.generationService = generationService;
    }

    @PostMapping
    public RagResponse query(@RequestBody String query) {
        return generationService.generateAnswer(query);
    }
}
