# Story 1.1: Project Initialization & Docker Orchestration

Status: ready-for-dev

## Story

As a developer,
I want to initialize the Spring Boot project and orchestrate the sidecar services (Ollama, Qdrant, Postgres) using Docker Compose,
so that I have a stable, reproducible development environment.

## Acceptance Criteria

1. **Given** a fresh development environment, **When** I run the initialization command, **Then** a Spring Boot 3.2 project is created with Maven and the required Spring AI milestone dependencies.
2. **Given** the project is initialized, **When** I run `docker-compose up`, **Then** Ollama, Qdrant, and Postgres containers start with the specified memory limits (JVM: 4GB, Ollama: 8GB, Qdrant: 2GB, Postgres: 1GB).

## Tasks / Subtasks

- [ ] Initialize Spring Boot Project (AC: 1)
  - [ ] Run curl command to generate Spring Boot 3.2 scaffold
  - [ ] Add Spring AI milestone repository to pom.xml
  - [ ] Add spring-ai-ollama and spring-ai-qdrant starters to pom.xml
- [ ] Configure Docker Orchestration (AC: 2)
  - [ ] Create docker-compose.yml with Ollama, Qdrant, and Postgres services
  - [ ] Implement memory limits and reservations per service (24GB budget)
  - [ ] Configure network and volume persistence for databases

## Dev Notes

- **Architecture Compliance**: Adhere to the 24GB RAM budget (JVM: 4GB, Ollama: 8GB, Qdrant: 2GB, Postgres: 1GB).
- **Spring AI**: Use milestone version (0.8.1 or latest available) and repository.
- **Docker**: Ensure hard `mem_limit` is set to prevent OOM.

### Project Structure Notes

- Root directory: `rag-service`
- Standard Maven structure.

### References

- [Source: _bmad-output/planning-artifacts/architecture.md#Architectural Decision: Memory Budgeting (24GB RAM)]
- [Source: _bmad-output/planning-artifacts/epics.md#Story 1.1: Project Initialization & Docker Orchestration]

## Dev Agent Record

### Agent Model Used

Gemini 3 Flash Preview

### Debug Log References

### Completion Notes List

### File List
