# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Shapyfy Core is a Spring Boot 3.5.7 backend service written in Kotlin 1.9.25, using Java 21. It's a REST API with PostgreSQL database, secured with Spring Security, and includes monitoring via Sentry and Spring Boot Actuator.

## Build & Development Commands

### Building
```bash
./gradlew build              # Full build including tests
./gradlew clean build        # Clean build from scratch
./gradlew bootJar           # Build executable JAR
```

### Running
```bash
./gradlew bootRun            # Run the application locally
```

Database connection is configured in `src/main/resources/application.yaml` pointing to a remote PostgreSQL instance.

### Testing
```bash
./gradlew test                                           # Run all tests
./gradlew test --tests ExerciseControllerIntegrationTest # Run single test class
./gradlew test --tests "*Exercise*"                     # Run tests matching pattern
```

**Integration Testing Setup**:
- Tests use Testcontainers with PostgreSQL 16 Alpine
- Base class: `IntegrationTestBase` in `src/test/kotlin/com/shapyfy/core/support/`
- Automatic database cleanup between tests (`@BeforeEach` clears exercises and translations)
- MockMvc configured with security filters disabled (`addFilters = false`)
- Helper method `postJson()` for making JSON POST requests with Accept-Language header
- Dynamic properties configure datasource to use Testcontainer instance

### Database Migrations

This project uses Flyway for database migrations. Migration files should be placed in:
```
src/main/resources/db/migration/
```

Name migrations following Flyway conventions: `V{version}__{description}.sql`
Example: `V1__create_users_table.sql`

## Architecture

### Technology Stack
- **Framework**: Spring Boot 3.5.7 with Spring Web MVC
- **Language**: Kotlin 1.9.25 with Java 21
- **Data Access**: Spring Data JDBC (not JPA)
- **Database**: PostgreSQL with Flyway migrations
- **Security**: Spring Security (default configuration)
- **Monitoring**:
  - Sentry (version 8.25.0) for error tracking
  - Spring Boot Actuator for health/metrics endpoints
  - Micrometer for custom metrics
- **Testing**: Testcontainers for PostgreSQL integration tests
- **Build Tool**: Gradle with Kotlin DSL

### Hexagonal Architecture (Ports and Adapters)

This codebase follows **hexagonal architecture** principles with clear separation of concerns:

**Domain Layer** (`domain/`):
- Contains core business logic and domain models
- Defines ports (interfaces) for core domain dependencies ONLY
- `ExerciseCreator` and `ExerciseFetcher` - Use cases/application services
- `ExercisePorts.kt` - Pure domain ports:
  - `ExerciseRepository` - persistence for Exercise aggregate
  - `ExerciseMetricsPort` - business event recording
- Domain models are in `domain/model/` (Exercise, Language, etc.)
- **Translation concerns moved to architecture layer** - domain doesn't know about i18n

**Boundary Layer** (`boundary/`):
- REST API adapters that translate HTTP requests/responses to domain commands
- `ExerciseController` - HTTP endpoints
- `ExerciseRestAdapter` - Maps between HTTP DTOs and domain models
- `ApiController` - Base controller with common constants (e.g., `ACCEPT_LANGUAGE_HEADER`)
- `ApiExceptionHandler` - Global exception handling
- `SyncException` - Custom exception for conflict responses

**Architecture Layer** (`architecture/`):
- Infrastructure adapters implementing domain ports
- Infrastructure-specific ports (e.g., translation) that aren't domain concerns
- `persistence/` - Spring Data JDBC repositories implementing `ExerciseRepository`
  - `ExerciseJdbcRepository` - adapter implementing the port
  - `ExerciseCrudRepositories.kt` - Spring Data JDBC interfaces
  - `ExerciseEntities.kt` - database entity classes (separate from domain models)
- `translation/` - Translation/i18n infrastructure
  - `TranslationPorts.kt` - **Architecture ports** for translation services (NOT domain!)
  - `TranslationPersistenceAdapter` - implements `TranslationCatalogPort`
  - `ExerciseNameTranslator` - orchestrates translation logic
- `ai/` - AI client implementations
  - `HeuristicAiTranslationClient` - implements `AiTranslationClient`
- `metrics/` - Micrometer-based metrics implementation
- `config/` - Spring configuration (e.g., `CacheConfig`)

**Key Architecture Principles**:
- Domain layer has no Spring dependencies (pure business logic)
- Domain defines ports ONLY for core business dependencies (Exercise repository, metrics)
- **Architecture layer has its own ports** for infrastructure concerns (translations, AI)
- Clear separation between domain models and database entities
- Controllers delegate to adapters, adapters delegate to domain services
- Translation flow: Controller → RestAdapter → NameTranslator (architecture) → Domain Service

**When to put ports in domain vs architecture:**
- ✅ **Domain port**: Core to business logic (e.g., `ExerciseRepository`)
- ❌ **Architecture port**: Technical concern (e.g., `TranslationCatalogPort`, `AiTranslationClient`)
- 🤔 **Borderline**: Observability/metrics (currently in domain, but could be architecture)

### Exception Handling Pattern for Business Conflicts

When domain operations fail due to business rule violations (e.g., duplicate exercises), follow this pattern:

**1. Domain throws pure exception** (no HTTP dependencies):
```kotlin
// domain/ExerciseDomainExceptions.kt
class ExerciseDuplicateException(val exercise: Exercise) : RuntimeException()

// domain/ExerciseCreator.kt
fun create(command: ExerciseCreationCommand): Exercise {
    val existing = exerciseRepository.findByName(command.canonicalName)
    if (existing != null) {
        metricsPort.recordConflict(Language.EN)  // Record metrics before throwing
        throw ExerciseDuplicateException(existing)
    }
    // ... create exercise
}
```

**2. Boundary layer catches and maps to HTTP response**:
```kotlin
// boundary/ApiExceptionHandler.kt
@ExceptionHandler(ExerciseDuplicateException::class)
fun handleExerciseDuplicate(exception: ExerciseDuplicateException): ResponseEntity<SyncErrorResponse> {
    // Map domain exception to HTTP 409 Conflict with localized translations
}
```

**Benefits**:
- Domain stays pure (no HTTP/Spring Web dependencies)
- Uses Spring's exception handler mechanism (clean, centralized)
- Metrics are recorded before exception is thrown
- Easy to test domain in isolation

### Multilingual Support

The application has built-in support for multilingual exercise names:
- Uses `Accept-Language` header (required on all endpoints)
- `Language` enum supports EN, PL, ES, DE, IT, FR
- Translation catalog with normalization for duplicate detection
- AI-based language detection and translation to canonical English names
- Translation keys (e.g., `exercise:squats`) link all language variants

### Database

**Connection**: Direct PostgreSQL connection configured in `application.yaml`:
- Host: `psql01.mikr.us:5432`
- Database: `db_adrian247` with schema `shapyfy`
- Uses Spring Data JDBC (not JPA - no lazy loading or proxies)

**Flyway Migrations**: Located in `src/main/resources/db/migration/`
- `V1__create_exercises.sql` - exercises table
- `V2__add_translations.sql` - translations catalog table

### Kotlin-specific Configuration
- JSR-305 strict mode enabled (`-Xjsr305=strict`)
- Kotlin reflection included for Spring
- Jackson Kotlin module for JSON serialization
- Note: Lombok is in dependencies but not typically used in Kotlin projects