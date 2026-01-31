package com.example.demo.dto;

import java.util.List;
import java.util.Map;

public record RagResponse(
    String correlationId,
    String originalQuery,
    String answer,
    Double confidenceScore,
    List<SourceMetadata> sources
) {}
