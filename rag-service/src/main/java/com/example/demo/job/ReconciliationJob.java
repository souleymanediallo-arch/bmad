package com.example.demo.job;

import com.example.demo.model.DocumentMetadata;
import com.example.demo.repository.DocumentMetadataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ReconciliationJob {

    private static final Logger logger = LoggerFactory.getLogger(ReconciliationJob.class);
    private final VectorStore vectorStore;
    private final DocumentMetadataRepository repository;

    public ReconciliationJob(VectorStore vectorStore, DocumentMetadataRepository repository) {
        this.vectorStore = vectorStore;
        this.repository = repository;
    }

    @Scheduled(cron = "0 0 2 * * *") // Every night at 2 AM
    public void reconcile() {
        logger.info("Starting nightly reconciliation job...");
        
        // This is a simplified version. In a real-world scenario, we would 
        // query Qdrant for all IDs and compare with Postgres.
        // For now, we'll just log the start of the process.
        
        List<DocumentMetadata> allMetadata = repository.findAll();
        Set<String> validVectorIds = allMetadata.stream()
                .flatMap(m -> m.getVectorIds().stream())
                .collect(Collectors.toSet());

        logger.info("Reconciliation complete. Found {} valid vector IDs across {} documents.", 
                validVectorIds.size(), allMetadata.size());
    }
}
