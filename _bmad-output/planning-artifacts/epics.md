---
stepsCompleted: [1, 2, 3, 4]
inputDocuments: ["brainstorming-session-2026-01-31-083617.md", "architecture.md", "ux-design-specification.md"]
project_name: "Reusable RAG Service"
---

# Reusable RAG Service - Epic Breakdown

## Overview

This document provides the complete epic and story breakdown for Reusable RAG Service, decomposing the requirements from the PRD, UX Design if it exists, and Architecture requirements into implementable stories.

## Requirements Inventory

### Functional Requirements

FR1: Ingest Text and PDF files into the RAG system.
FR2: Add new files to the knowledge base dynamically.
FR3: Delete existing files from the knowledge base dynamically.
FR4: Retrieve information based on user input via REST service endpoints.
FR5: Expose operations as REST service endpoints.
FR6: Support 1-100 documents initially.
FR7: Log user questions and answers (future requirement).

### NonFunctional Requirements

NFR1: High-quality retrieval outputs.
NFR2: Independent and reusable architecture.
NFR3: Optimized for modest hardware (24GB RAM, 8 Cores, 1TB Disk).
NFR4: Sub-second API responsiveness for "Warm" models.
NFR5: Self-healing model management via Spring Actuator.
NFR6: Full audit trail and observability in Postgres.

### Additional Requirements

- **Starter Template**: Spring Initializr + Spring AI (Milestone) with Java/Maven.
- **Memory Budget**: Strict limits (JVM: 4GB, Ollama: 8GB, Qdrant: 2GB, Postgres: 1GB).
- **Retrieval Pipeline**: 3-stage (Hybrid Search 70/30 -> BGE-Reranker -> Top-5 Context).
- **Resilience**: Request Semaphore for concurrent inference, Nightly Reconciliation Job, Startup Warm-up Service.
- **API Contract**: JSON response including correlation_id, original_query, confidence_score, and sources with reranker_score.

### FR Coverage Map

FR1: Epic 2 - Ingest Text and PDF files
FR2: Epic 2 - Add new files dynamically
FR3: Epic 2 - Delete existing files dynamically
FR4: Epic 3 - Retrieve info via REST
FR5: Epic 1 & 3 - Expose REST endpoints
FR6: Epic 1 - Support 1-100 docs
FR7: Epic 4 - Log user questions and answers

## Epic List

### Epic 1: Core Service Infrastructure & Model Orchestration
Developers can start the service and verify that the "Warm" models (Ollama) and databases (Qdrant/Postgres) are ready for use.
**FRs covered:** FR5, FR6.

### Epic 2: Dynamic Knowledge Base Management (CRUD)
Developers can programmatically add and delete PDF/Text files, with the system automatically handling chunking and indexing.
**FRs covered:** FR1, FR2, FR3.

### Epic 3: High-Precision Information Retrieval
Developers can query the knowledge base and receive high-quality, verified answers with full source transparency.
**FRs covered:** FR4, FR5.

### Epic 4: Observability & Quality Feedback Loop
Developers can audit system performance, trace queries via correlation IDs, and collect user feedback to improve the RAG quality.
**FRs covered:** FR7.

## Epic 1: Core Service Infrastructure & Model Orchestration
Developers can start the service and verify that the "Warm" models (Ollama) and databases (Qdrant/Postgres) are ready for use.

### Story 1.1: Project Initialization & Docker Orchestration
As a developer,
I want to initialize the Spring Boot project and orchestrate the sidecar services (Ollama, Qdrant, Postgres) using Docker Compose,
So that I have a stable, reproducible development environment.

**Acceptance Criteria:**
**Given** a fresh development environment,
**When** I run the initialization command,
**Then** a Spring Boot 3.2 project is created with Maven and the required Spring AI milestone dependencies.
**Given** the project is initialized,
**When** I run `docker-compose up`,
**Then** Ollama, Qdrant, and Postgres containers start with the specified memory limits (JVM: 4GB, Ollama: 8GB, Qdrant: 2GB, Postgres: 1GB).

### Story 1.2: Self-Healing Health Checks & Model Warm-up
As a system administrator,
I want the service to automatically verify and "warm up" the required models in Ollama on startup,
So that the service is resilient and ready for sub-second queries immediately.

**Acceptance Criteria:**
**Given** the service is starting up,
**When** the application context is initialized,
**Then** a "Startup Warm-up Service" triggers a dummy inference to Ollama to ensure the models are loaded in RAM.
**Given** the service is running,
**When** I call the `/health/rag` Actuator endpoint,
**Then** I receive a JSON response showing the "Warm" status of the models and the connectivity to Qdrant and Postgres.

## Epic 2: Dynamic Knowledge Base Management (CRUD)
Developers can programmatically add and delete PDF/Text files, with the system automatically handling chunking and indexing.

### Story 2.1: Dynamic Document Ingestion (PDF/Text)
As a developer,
I want to POST a PDF or Text file to the service,
So that it is automatically chunked, embedded, and indexed in Qdrant.

**Acceptance Criteria:**
**Given** a valid PDF or Text file (under 50MB),
**When** I POST it to `/api/v1/documents`,
**Then** the system splits the text into 512-token chunks with 50-token overlap.
**Given** the chunks are generated,
**When** the embedding process is complete,
**Then** the vectors are stored in Qdrant and the metadata (file name, page number) is stored in Postgres.

### Story 2.2: Dynamic Document Deletion & Reconciliation
As a developer,
I want to DELETE a document by its ID,
So that it is immediately removed from the RAG index and the knowledge base remains accurate.

**Acceptance Criteria:**
**Given** an existing document ID,
**When** I send a DELETE request to `/api/v1/documents/{id}`,
**Then** all associated vectors are purged from Qdrant and the metadata is removed from Postgres in a single transaction.
**Given** the system is running,
**When** the nightly reconciliation job executes,
**Then** it identifies and removes any "orphan" vectors in Qdrant that do not have matching metadata in Postgres.

## Epic 3: High-Precision Information Retrieval
Developers can query the knowledge base and receive high-quality, verified answers with full source transparency.

### Story 3.1: Hybrid Search & Reranking Pipeline
As a developer,
I want to query the RAG service using a combination of semantic and keyword search,
So that I get the most relevant document snippets for the LLM to process.

**Acceptance Criteria:**
**Given** a user query,
**When** the retrieval process starts,
**Then** Qdrant performs a Hybrid Search (70% Vector, 30% BM25) to find the top 20 candidates.
**Given** the top 20 candidates,
**When** the BGE-Reranker model processes them,
**Then** only the top 5 chunks with the highest reranker scores are selected for the final context.

### Story 3.2: High-Quality Generation & API Response
As a developer,
I want to receive a structured JSON response containing the answer and full source metadata,
So that I can provide a transparent and trustworthy experience to my end users.

**Acceptance Criteria:**
**Given** the top 5 verified chunks,
**When** the LLM (Qwen/Gemma) generates the answer,
**Then** the API returns the "Perfect Response" JSON structure, including `correlation_id`, `original_query`, `confidence_score`, and `sources`.
**Given** multiple simultaneous queries,
**When** the system reaches its concurrency limit,
**Then** the Request Semaphore ensures that additional queries are queued rather than causing an OOM crash.

## Epic 4: Observability & Quality Feedback Loop
Developers can audit system performance, trace queries via correlation IDs, and collect user feedback to improve the RAG quality.

### Story 4.1: Postgres Audit Logging & Traceability
As a system administrator,
I want every query and answer to be logged in Postgres with its correlation ID and confidence score,
So that I can audit the system's performance and troubleshoot quality issues.

**Acceptance Criteria:**
**Given** a query is processed,
**When** the response is generated,
**Then** a record is created in the Postgres `query_logs` table containing the `correlation_id`, `original_query`, `answer`, `confidence_score`, and `processing_time_ms`.
**Given** the system is running,
**When** I query the Postgres database,
**Then** I can retrieve the full audit trail for any specific `correlation_id`.

### Story 4.2: User Feedback API & Quality Loop
As a developer,
I want to submit user feedback (thumbs up/down) for a specific RAG answer,
So that the system can collect data for future quality improvements.

**Acceptance Criteria:**
**Given** a valid `correlation_id`,
**When** I send a POST request to `/api/v1/feedback` with a `rating` (e.g., 1 or -1),
**Then** the corresponding record in the Postgres `query_logs` table is updated with the feedback value.
**Given** the feedback is stored,
**When** I analyze the logs,
**Then** I can identify queries where the system had high confidence but received negative user feedback.

### FR Coverage Map (Final)
- FR1: Story 2.1
- FR2: Story 2.1
- FR3: Story 2.2
- FR4: Story 3.1, 3.2
- FR5: Story 1.1, 3.2
- FR6: Story 1.1
- FR7: Story 4.1, 4.2

---
session_active: false
workflow_completed: true
stepsCompleted: [1, 2, 3, 4]
---
