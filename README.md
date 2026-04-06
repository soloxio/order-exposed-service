# Order Exposed Service

This service exposes a public HTTP API for the UI or external callers.
It accepts TMF622-style order payloads and forwards them to the orchestration service.

## Build and run
```bash
./mvnw clean test
./mvnw quarkus:dev
```