package com.example.demo.dto;

public record SourceMetadata(
    String fileName,
    Integer pageNumber,
    Double rerankerScore,
    String content
) {}
