# Story 1.2: Self-Healing Health Checks & Model Warm-up

Status: in-progress

## Story

As a system administrator,
I want the service to automatically verify and "warm up" the required models in Ollama on startup,
so that the service is resilient and ready for sub-second queries immediately.

## Acceptance Criteria

1. **Given** the service is starting up, **When** the application context is initialized, **Then** a "Startup Warm-up Service" triggers a dummy inference to Ollama to ensure the models are loaded in RAM.
2. **Given** the service is running, **When** I call the `/health/rag` Actuator endpoint, **Then** I receive a JSON response showing the "Warm" status of the models and the connectivity to Qdrant and Postgres.

## Tasks / Subtasks

- [ ] Implement Startup Warm-up Service (AC: 1)
  - [ ] Create `ModelWarmupService` using `ApplicationListener<ApplicationReadyEvent>`
  - [ ] Inject `OllamaChatClient` and perform a simple prompt
- [ ] Implement Custom Health Indicator (AC: 2)
  - [ ] Create `RagHealthIndicator` implementing `HealthIndicator`
  - [ ] Check connectivity to Ollama, Qdrant, and Postgres
  - [ ] Expose via `/actuator/health/rag`

## Dev Notes

- **Ollama Model**: Ensure the model name matches what's configured in `application.properties`.
- **Actuator**: Ensure `management.endpoints.web.exposure.include` includes `health`.

## Dev Agent Record

### Agent Model Used

Gemini 3 Flash Preview

### Debug Log References

### Completion Notes List

### File List
