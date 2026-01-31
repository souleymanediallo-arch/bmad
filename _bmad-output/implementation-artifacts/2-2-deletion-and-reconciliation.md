# Story 2.2: Dynamic Document Deletion & Reconciliation

Status: in-progress

## Story

As a developer,
I want to DELETE a document by its ID,
so that it is immediately removed from the RAG index and the knowledge base remains accurate.

## Acceptance Criteria

1. **Given** an existing document ID, **When** I send a DELETE request to `/api/v1/documents/{id}`, **Then** all associated vectors are purged from Qdrant and the metadata is removed from Postgres in a single transaction.
2. **Given** the system is running, **When** the nightly reconciliation job executes, **Then** it identifies and removes any "orphan" vectors in Qdrant that do not have matching metadata in Postgres.

## Tasks / Subtasks

- [ ] Implement Document Deletion (AC: 1)
  - [ ] Update `DocumentController` with DELETE `/api/v1/documents/{id}`
  - [ ] Update `DocumentService` to handle deletion from Qdrant and Postgres
- [ ] Implement Reconciliation Job (AC: 2)
  - [ ] Create `ReconciliationJob` using `@Scheduled`
  - [ ] Implement logic to find and remove orphan vectors

## Dev Notes

- **Qdrant Deletion**: Use `vectorStore.delete(List<String> ids)`. We need to track the vector IDs associated with each document.
- **Metadata Update**: We might need to store the vector IDs in the `DocumentMetadata` entity to make deletion easier.

## Dev Agent Record

### Agent Model Used

Gemini 3 Flash Preview

### Debug Log References

### Completion Notes List

### File List
