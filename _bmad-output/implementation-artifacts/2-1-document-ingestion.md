# Story 2.1: Dynamic Document Ingestion (PDF/Text)

Status: in-progress

## Story

As a developer,
I want to POST a PDF or Text file to the service,
so that it is automatically chunked, embedded, and indexed in Qdrant.

## Acceptance Criteria

1. **Given** a valid PDF or Text file (under 50MB), **When** I POST it to `/api/v1/documents`, **Then** the system splits the text into 512-token chunks with 50-token overlap.
2. **Given** the chunks are generated, **When** the embedding process is complete, **Then** the vectors are stored in Qdrant and the metadata (file name, page number) is stored in Postgres.

## Tasks / Subtasks

- [ ] Implement Ingestion API (AC: 1, 2)
  - [ ] Create `DocumentController` with POST `/api/v1/documents`
  - [ ] Create `DocumentService` to handle ingestion logic
  - [ ] Configure `TokenTextSplitter` (512 tokens, 50 overlap)
  - [ ] Integrate `VectorStore` (Qdrant) for storage
  - [ ] Create JPA Entity for document metadata in Postgres

## Dev Notes

- **Spring AI**: Use `TikaDocumentReader` for PDF/Text support.
- **Chunking**: `TokenTextSplitter` is preferred for LLM compatibility.
- **Metadata**: Ensure `file_name` and `page_number` are preserved in the `Document` metadata.

## Dev Agent Record

### Agent Model Used

Gemini 3 Flash Preview

### Debug Log References

### Completion Notes List

### File List
