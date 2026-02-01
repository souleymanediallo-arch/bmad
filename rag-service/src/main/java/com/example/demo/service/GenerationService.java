package com.example.demo.service;

import com.example.demo.dto.RagResponse;
import com.example.demo.dto.SourceMetadata;
import com.example.demo.model.QueryLog;
import com.example.demo.repository.QueryLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GenerationService {

    private static final Logger logger = LoggerFactory.getLogger(GenerationService.class);
    private final OllamaChatModel chatClient;
    private final RetrievalService retrievalService;
    private final QueryLogRepository queryLogRepository;
    private final ChatMemory chatMemory;

    public GenerationService(OllamaChatModel chatClient, RetrievalService retrievalService, 
                             QueryLogRepository queryLogRepository, ChatMemory chatMemory) {
        this.chatClient = chatClient;
        this.retrievalService = retrievalService;
        this.queryLogRepository = queryLogRepository;
        this.chatMemory = chatMemory;
    }

    public RagResponse generateAnswer(String query, String sessionId) {
        long startTime = System.currentTimeMillis();
        String correlationId = UUID.randomUUID().toString();
        logger.info("[{}] Processing RAG query for session {}: {}", correlationId, sessionId, query);

        // 1. Retrieve Context
        List<Document> contextDocs = retrievalService.retrieveContext(query);
        String context = contextDocs.stream()
                .map(Document::getContent)
                .collect(Collectors.joining("\n\n"));

        // 2. Build Prompt with History
        List<Message> messages = new ArrayList<>();
        
        // System Message with Context
        messages.add(new SystemMessage("""
                Tu es l'assistant de la Clinique Coumba. Tu t'appelles Chatbot Coumba. Tu n'es PAS un médecin. Sois concis.
                
                CONSIGNES :
                1. Réponds en deux phrases maximum.
                2. UNIQUEMENT si l'utilisateur demande explicitement à créer un ticket, prendre rendez-vous ou s'inscrire, réponds EXACTEMENT par le mot : 'ACTION_TICKET'.
                3. Utilise l'historique pour comprendre les questions de suivi.
                
                Context:
                """ + context));

        // Add Chat History
        List<Message> history = chatMemory.get(sessionId, 10); // Last 10 messages
        messages.addAll(history);

        // Add Current User Message
        UserMessage userMessage = new UserMessage(query);
        messages.add(userMessage);

        Prompt prompt = new Prompt(messages);

        // 3. Generate Answer
        ChatResponse response = chatClient.call(prompt);
        String answer = response.getResult().getOutput().getContent();

        // 4. Update Chat Memory
        chatMemory.add(sessionId, userMessage);
        chatMemory.add(sessionId, response.getResult().getOutput());

        // 5. Map Sources
        List<SourceMetadata> sources = contextDocs.stream()
                .map(doc -> new SourceMetadata(
                        (String) doc.getMetadata().get("file_name"),
                        (Integer) doc.getMetadata().get("page_number"),
                        1.0, // Placeholder for reranker score
                        doc.getContent()
                ))
                .collect(Collectors.toList());

        long endTime = System.currentTimeMillis();
        long processingTimeMs = endTime - startTime;

        // 5. Audit Log
        QueryLog log = new QueryLog();
        log.setCorrelationId(correlationId);
        log.setOriginalQuery(query);
        log.setAnswer(answer);
        log.setConfidenceScore(1.0); // Placeholder
        log.setProcessingTimeMs(processingTimeMs);
        queryLogRepository.save(log);

        logger.info("[{}] Generation complete in {}ms.", correlationId, processingTimeMs);
        return new RagResponse(
                correlationId,
                query,
                answer,
                1.0, // Placeholder for confidence score
                sources
        );
    }
}
