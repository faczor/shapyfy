# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Shapyfy Core is a Spring Boot 3.2 fitness training platform backend built with Java 21. It manages training plans, workout tracking, exercise catalogs, and user activity logging with a PostgreSQL database.

## Build and Development Commands

### Building and Testing
```bash
# Build the project
mvn clean install

# Run tests
mvn test

# Run a single test class
mvn test -Dtest=CalendarMapperUnitTest

# Run a single test method
mvn test -Dtest=CalendarMapperUnitTest#testMethodName

# Run the application
mvn spring-boot:run

# Run with specific profile
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

### Database
- Uses Flyway for database migrations
- Migration files: `src/main/resources/db/migration/`
- Naming convention: `V{version}__{description}.sql` (note double underscore)
- PostgreSQL is the production database

## Architecture

### Hexagonal Architecture (Ports & Adapters)

The codebase follows hexagonal architecture with clear separation:

**Domain Layer** (`com.shapyfy.core.domain`)
- Core business logic and domain models
- `domain/model/`: Domain entities (Exercise, PlanDay, TrainingPlan, ActivityLog, UserId, WorkoutSet, etc.)
- `domain/port/`: Repository interfaces (ports) for infrastructure
- Domain services: `PlanDays`, `ActivityLogs`, `Exercises`, `TrainingPlanFetcher`

**Boundary Layer** (`com.shapyfy.core.boundary`)
- REST API controllers and DTOs
- Structure: `boundary/api/{feature}/`
  - Controllers: Handle HTTP requests (e.g., `DashboardController`, `TrainingController`, `PlanDayController`)
  - `adapter/`: Maps between domain models and API contracts
  - `model/`: API request/response DTOs (Contract classes)
- Authentication via JWT token in Authorization header, parsed by `TokenUtils.currentUserId()`

**Infrastructure Layer** (`com.shapyfy.core.infrastructure`)
- `configuration/`: Spring Security, app config
- `storage/postgres/`: JPA repository implementations
  - Adapters implement domain ports (e.g., `PlanDayAdapter` implements `PlanDayRepository`)
  - JPA repositories extend Spring Data interfaces (e.g., `PlanDayJpaRepository`)

### Key Architectural Patterns

**Adapter Pattern**: Infrastructure adapters implement domain ports
```
Domain Port (interface) → Infrastructure Adapter (implementation) → JPA Repository
Example: PlanDayRepository → PlanDayAdapter → PlanDayJpaRepository
```

**Strategy Pattern**: Calendar mapping uses different strategies based on context
- `CalendarMappingStrategy` interface
- `CalendarActivityLogBasedMappingStrategy`: Maps calendar based on activity logs
- `CalendarTrainingPlanBasedMappingStrategy`: Maps calendar based on training plan start date
- Strategy selected in `CalendarMapper` based on whether activity log or plan start date is closer

**Authentication**:
- JWT tokens parsed in `TokenUtils`
- Extracts user_id from JWT payload to create `UserId` domain object
- Spring Security currently configured to permit all requests (`RestSecurityConfiguration`)

## Domain Concepts

**Training Plan**: Collection of PlanDays defining a user's workout schedule
**PlanDay**: A single day in a training plan (WORKOUT_DAY or REST_DAY)
**ActivityLog**: Record of a completed workout session
**Exercise**: Exercise definition from catalog
**WorkoutExerciseConfig**: Configuration of an exercise within a workout (sets, reps, weight)
**WorkoutSet**: Individual set performed during a workout

**Calendar Logic**:
- Calendar generation determines workout schedule visibility
- Uses closer reference point between plan start date and first activity log
- Implemented via strategy pattern in `CalendarMapper`

## Technology Stack

- **Framework**: Spring Boot 3.2.0
- **Java**: 21
- **Database**: PostgreSQL with Flyway migrations
- **Security**: Spring Security with JWT
- **ORM**: Spring Data JPA
- **Build**: Maven
- **Documentation**: OpenAPI/Swagger (springdoc-openapi)
- **Utilities**: Lombok, Guava

## Testing

- Tests mirror src structure: `src/test/java/com/shapyfy/core/{boundary|domain}/`
- Unit tests use standard JUnit 5
- Example: `CalendarMapperUnitTest` tests calendar mapping logic

## API Documentation

OpenAPI documentation available at `/api-docs` endpoint (when running)
