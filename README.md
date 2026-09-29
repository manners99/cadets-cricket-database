# Cadets Cricket Database

Java/Spring Boot application for the Cadets Cricket Club database.

## Technology

- Java 21
- Spring Boot
- Spring Data JPA
- SQLite
- Flyway database migrations
- Maven

## Project layout

```text
src/
  main/
    java/uk/co/cadetscricket/
      CadetsCricketDatabaseApplication.java
    resources/
      application.properties
      db/migration/
        V1__create_application_metadata.sql
```

The domain packages will be added after the client-approved data model is confirmed. Suggested future packages include `player`, `team`, `season`, `match`, `attendance`, and `security`.

## Running locally

Install Java 21 and Maven, then run:

```powershell
mvn spring-boot:run
```

The SQLite database will be created at `data/cadets-cricket.db`. Database changes should be added as new Flyway migrations under `src/main/resources/db/migration`; do not edit migrations that have already been applied.
