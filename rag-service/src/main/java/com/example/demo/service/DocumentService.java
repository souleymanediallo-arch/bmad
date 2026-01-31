package com.example.demo.service;

import com.example.demo.model.DocumentMetadata;
import com.example.demo.repository.DocumentMetadataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class DocumentService {

    private static final Logger logger = LoggerFactory.getLogger(DocumentService.class);
    private final VectorStore vectorStore;
    private final DocumentMetadataRepository repository;

    public DocumentService(VectorStore vectorStore, DocumentMetadataRepository repository) {
        this.vectorStore = vectorStore;
        this.repository = repository;
    }

    @Transactional
    public void ingestDocument(MultipartFile file) throws IOException {
        logger.info("Ingesting document: {}", file.getOriginalFilename());

        // 1. Read the document
        TikaDocumentReader reader = new TikaDocumentReader(new InputStreamResource(file.getInputStream()));
        List<Document> documents = reader.get();

        // 2. Split the document into chunks
        TokenTextSplitter splitter = new TokenTextSplitter(512, 50, 5, 10000, true);
        List<Document> chunks = splitter.apply(documents);

        // 3. Add metadata to chunks
        for (Document chunk : chunks) {
            chunk.getMetadata().put("file_name", file.getOriginalFilename());
            // Tika might provide page numbers, but for now we ensure the file name is there
        }

        // 4. Store in Vector Store (Qdrant)
        vectorStore.add(chunks);

        // 5. Save metadata in Postgres
        DocumentMetadata metadata = new DocumentMetadata();
        metadata.setFileName(file.getOriginalFilename());
        metadata.setContentType(file.getContentType());
        metadata.setFileSize(file.getSize());
        metadata.setVectorIds(chunks.stream().map(Document::getId).toList());
        repository.save(metadata);

        logger.info("Successfully ingested document: {} ({} chunks)", file.getOriginalFilename(), chunks.size());
    }

    @Transactional
    public void deleteDocument(Long id) {
        repository.findById(id).ifPresent(metadata -> {
            logger.info("Deleting document: {}", metadata.getFileName());
            vectorStore.delete(metadata.getVectorIds());
            repository.delete(metadata);
            logger.info("Successfully deleted document: {}", metadata.getFileName());
        });
    }
}
