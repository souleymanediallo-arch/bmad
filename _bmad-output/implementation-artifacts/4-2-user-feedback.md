# Story 4.2: User Feedback API & Quality Loop

Status: in-progress

## Story

As a developer,
I want to submit user feedback (thumbs up/down) for a specific RAG answer,
so that the system can collect data for future quality improvements.

## Acceptance Criteria

1. **Given** a valid `correlation_id`, **When** I send a POST request to `/api/v1/feedback` with a `rating` (e.g., 1 or -1), **Then** the corresponding record in the Postgres `query_logs` table is updated with the feedback value.
2. **Given** the feedback is stored, **When** I analyze the logs, **Then** I can identify queries where the system had high confidence but received negative user feedback.

## Tasks / Subtasks

- [ ] Implement Feedback API (AC: 1, 2)
  - [ ] Create `FeedbackRequest` DTO
  - [ ] Create `FeedbackController` with POST `/api/v1/feedback`
  - [ ] Update `QueryLogRepository` with a custom update method (or use standard save)
  - [ ] Implement feedback logic in a new `FeedbackService`

## Dev Notes

- **Validation**: Ensure the `correlation_id` exists before attempting to update.
- **Rating**: Use 1 for positive feedback and -1 for negative feedback.

## Dev Agent Record

### Agent Model Used

Gemini 3 Flash Preview

### Debug Log References

### Completion Notes List

### File List
