package com.example.demo.service;

import com.example.demo.dto.RagResponse;
import com.example.demo.dto.SourceMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GenerationService {

    private static final Logger logger = LoggerFactory.getLogger(GenerationService.class);
    private final OllamaChatClient chatClient;
    private final RetrievalService retrievalService;

    public GenerationService(OllamaChatClient chatClient, RetrievalService retrievalService) {
        this.chatClient = chatClient;
        this.retrievalService = retrievalService;
    }

    public RagResponse generateAnswer(String query) {
        String correlationId = UUID.randomUUID().toString();
        logger.info("[{}] Processing RAG query: {}", correlationId, query);

        // 1. Retrieve Context
        List<Document> contextDocs = retrievalService.retrieveContext(query);
        String context = contextDocs.stream()
                .map(Document::getContent)
                .collect(Collectors.joining("\n\n"));

        // 2. Build Prompt
        SystemMessage systemMessage = new SystemMessage("""
                You are a helpful assistant. Use the provided context to answer the user's question.
                If the answer is not in the context, say you don't know.
                
                Context:
                """ + context);
        UserMessage userMessage = new UserMessage(query);
        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));

        // 3. Generate Answer
        ChatResponse response = chatClient.call(prompt);
        String answer = response.getResult().getOutput().getContent();

        // 4. Map Sources
        List<SourceMetadata> sources = contextDocs.stream()
                .map(doc -> new SourceMetadata(
                        (String) doc.getMetadata().get("file_name"),
                        (Integer) doc.getMetadata().get("page_number"),
                        1.0, // Placeholder for reranker score
                        doc.getContent()
                ))
                .collect(Collectors.toList());

        logger.info("[{}] Generation complete.", correlationId);
        return new RagResponse(
                correlationId,
                query,
                answer,
                1.0, // Placeholder for confidence score
                sources
        );
    }
}
