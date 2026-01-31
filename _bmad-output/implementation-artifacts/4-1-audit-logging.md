# Story 4.1: Postgres Audit Logging & Traceability

Status: in-progress

## Story

As a system administrator,
I want every query and answer to be logged in Postgres with its correlation ID and confidence score,
so that I can audit the system's performance and troubleshoot quality issues.

## Acceptance Criteria

1. **Given** a query is processed, **When** the response is generated, **Then** a record is created in the Postgres `query_logs` table containing the `correlation_id`, `original_query`, `answer`, `confidence_score`, and `processing_time_ms`.
2. **Given** the system is running, **When** I query the Postgres database, **Then** I can retrieve the full audit trail for any specific `correlation_id`.

## Tasks / Subtasks

- [ ] Implement Audit Logging (AC: 1, 2)
  - [ ] Create `QueryLog` JPA entity
  - [ ] Create `QueryLogRepository`
  - [ ] Update `GenerationService` to log each request/response
  - [ ] Add `processing_time_ms` calculation

## Dev Notes

- **Performance**: Ensure logging doesn't significantly impact API responsiveness.
- **Traceability**: The `correlation_id` should be the primary key or a highly indexed field.

## Dev Agent Record

### Agent Model Used

Gemini 3 Flash Preview

### Debug Log References

### Completion Notes List

### File List
