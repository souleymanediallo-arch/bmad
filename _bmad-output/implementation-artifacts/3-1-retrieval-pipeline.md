# Story 3.1: Hybrid Search & Reranking Pipeline

Status: in-progress

## Story

As a developer,
I want to query the RAG service using a combination of semantic and keyword search,
so that I get the most relevant document snippets for the LLM to process.

## Acceptance Criteria

1. **Given** a user query, **When** the retrieval process starts, **Then** Qdrant performs a Hybrid Search (70% Vector, 30% BM25) to find the top 20 candidates.
2. **Given** the top 20 candidates, **When** the BGE-Reranker model processes them, **Then** only the top 5 chunks with the highest reranker scores are selected for the final context.

## Tasks / Subtasks

- [ ] Implement Retrieval Pipeline (AC: 1, 2)
  - [ ] Create `RetrievalService` to handle search logic
  - [ ] Configure `VectorStore` for Hybrid Search (if supported by Spring AI Qdrant starter)
  - [ ] Integrate a Reranker (e.g., using a local model or a specialized service)
  - [ ] Implement the 3-stage pipeline (Search -> Rerank -> Select Top-5)

## Dev Notes

- **Hybrid Search**: Check if the current Spring AI Qdrant starter supports hybrid search natively. If not, we may need to perform two searches and combine them.
- **Reranker**: We'll need to decide on the best way to implement the BGE-Reranker. A local Ollama model or a dedicated library might be options.

## Dev Agent Record

### Agent Model Used

Gemini 3 Flash Preview

### Debug Log References

### Completion Notes List

### File List
