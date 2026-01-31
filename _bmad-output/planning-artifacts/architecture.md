---
stepsCompleted: [1, 2]
inputDocuments: ['brainstorming-session-2026-01-31-083617.md']
project_name: 'Reusable RAG Service'
---

# Architecture Decisions: Reusable RAG Service

## Project Context Analysis

### Requirements Overview

**Functional Requirements:**
- REST API for document management (Add/Delete).
- REST API for information retrieval (Query).
- Support for PDF and Text file ingestion.
- Dynamic index updates (no manual re-indexing).

**Non-Functional Requirements:**
- **Performance:** Sub-second response for "Warm" models.
- **Quality:** High-precision retrieval using Hybrid Search and Reranking.
- **Resilience:** Self-healing model management.
- **Observability:** Full audit trail in Postgres.

**Scale & Complexity:**
- Primary domain: RAG Service / API
- Complexity level: Medium
- Estimated architectural components: 4 (App, Ollama, Qdrant, Postgres)

### Technical Constraints & Dependencies
- Hardware: 24GB RAM, 8 Cores, 1TB Disk.
- Stack: Java/Spring Boot, Spring AI, Ollama, Qdrant, Postgres.

### Cross-Cutting Concerns Identified
- Memory Budgeting (24GB limit).
- Correlation ID tracking across services.
- Model "Warm-up" and eviction logic.

## Architectural Decision: Memory Budgeting (24GB RAM)

**Decision:** We will implement a strict memory allocation strategy using Docker Compose resource limits to ensure system stability and "Warm" model performance.

**Memory Allocation Table:**
| Component | RAM Allocation | Docker Limit |
| :--- | :--- | :--- |
| OS / Docker Overhead | 2 GB | N/A |
| Spring Boot (JVM) | 4 GB | 4.5 GB |
| Ollama (Models + Cache) | 8 GB | 12 GB (Burst) |
| Qdrant (Vector DB) | 2 GB | 2.5 GB |
| Postgres (Logs/Meta) | 1 GB | 1.5 GB |
| **Total Reserved** | **17 GB** | **20.5 GB** |

**Rationale:**
- **JVM:** 4GB is sufficient for Spring AI and PDF parsing without excessive GC pauses.
- **Ollama:** 8GB allows for a 7B model (5GB) + Embedding model (1GB) + Reranker (1GB) to stay resident.
- **Buffer:** A 3.5GB - 7GB buffer is maintained to handle KV Cache spikes during long context inference.

**Implementation Note:**
The Spring Boot application must be started with `-Xmx4g` and `-XX:+UseZGC` to optimize memory usage.

## Risk Mitigation & Resilience (Pre-mortem Results)

**Identified Risks & Preventative Measures:**

1. **Concurrent Inference Overload:**
   - *Risk*: Multiple simultaneous queries exceeding RAM/CPU limits.
   - *Mitigation*: Implement a **Request Semaphore** in Spring Boot to limit active LLM inferences to 1-2 at a time.

2. **Index Drift (Zombie Documents):**
   - *Risk*: Discrepancy between Postgres metadata and Qdrant vector index.
   - *Mitigation*: Implement a nightly **Reconciliation Job** to synchronize the two stores.

3. **Context Window Bloat:**
   - *Risk*: Large PDFs or too many retrieved chunks slowing down the Reranker/LLM.
   - *Mitigation*: Enforce strict **Max File Size** (e.g., 50MB) and **Max Chunk Limits** per query.

4. **Cold Start Latency:**
   - *Risk*: First query timeout while Ollama loads models from disk.
   - *Mitigation*: Implement a **Startup Warm-up Service** that triggers a dummy inference on application boot.
