package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RetrievalService {

    private static final Logger logger = LoggerFactory.getLogger(RetrievalService.class);
    private final VectorStore vectorStore;

    public RetrievalService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    /**
     * 3-Stage Retrieval Pipeline:
     * 1. Search: Retrieve top 20 candidates using Vector Search (Hybrid placeholder)
     * 2. Rerank: (Placeholder for BGE-Reranker)
     * 3. Select: Return top 5
     */
    public List<Document> retrieveContext(String query) {
        logger.info("Starting retrieval pipeline for query: {}", query);

        // Stage 1: Search (Top 20)
        // Note: Spring AI Qdrant starter currently defaults to vector search.
        // Hybrid search (70/30) would require custom Qdrant client calls or 
        // waiting for native Spring AI support for Hybrid Search.
        List<Document> candidates = vectorStore.similaritySearch(
                SearchRequest.query(query).withTopK(20)
        );
        logger.debug("Retrieved {} candidates from vector store", candidates.size());

        // Stage 2: Rerank (Placeholder)
        // In a full implementation, we would call a BGE-Reranker model here.
        // For now, we simulate by keeping the order or applying a simple heuristic.
        List<Document> reranked = candidates; 

        // Stage 3: Select Top 5
        List<Document> result = reranked.stream()
                .limit(5)
                .collect(Collectors.toList());

        logger.info("Retrieval pipeline complete. Returning top {} chunks.", result.size());
        return result;
    }
}
