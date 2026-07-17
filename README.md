# My Personal ESV Bible

This project is a personal Bible reading tracker built to replace a Google Sheets-based workflow with a cleaner web application. Version 1 focuses on tracking reading progress across all 66 books and 1,189 chapters of the Bible. The app will let a user mark chapters complete or incomplete, filter by testament, and view reading summaries. After the tracker is stable, the app will support opening a chapter and displaying ESV passage text through the Crossway API.

## Tech Stack

- Java 17
- Spring Boot
- Spring Data JPA
- Hibernate
- SQLite
- React with TypeScript, planned for the frontend

## Local Setup

### Prerequisites

- Java 17 or newer
- Maven
- SQLite, optional but useful for inspecting the local database
- An ESV Crossway API key

### Environment Variables

The backend expects the ESV API key to be provided through an environment variable:

```zsh
export ESV_CROSSWAY_API_KEY="your-api-key-here"
```

If you add this to your shell profile, use `~/.zshrc` since the local shell is zsh.

### Database

The application is configured to use SQLite:

```properties
spring.datasource.url=jdbc:sqlite:data/esv-bible.db
```

Hibernate is currently configured with:

```properties
spring.jpa.hibernate.ddl-auto=update
```

This is useful while learning because Hibernate can create or update tables from the JPA entities. A future production-oriented version should likely use explicit database migrations.

### Run Tests

Use system Maven:

```zsh
mvn test
```

To run only the Bible catalog tests:

```zsh
mvn test -Dtest=BibleBookTest
```

To run only the completed chapter repository tests:

```zsh
mvn test -Dtest=CompletedChapterRepositoryTest
```

### Run the Application

```zsh
mvn spring-boot:run
```

The app currently requires `ESV_CROSSWAY_API_KEY` to be set because the ESV client reads it from configuration.

## Current Implementation Status

Completed so far:

- Bible catalog modeled as a Java enum
- Old Testament and New Testament classification
- Catalog integrity tests for book counts, chapter counts, and boundaries
- SQLite/JPA dependencies added
- `CompletedChapterId` composite key modeled
- `CompletedChapter` JPA entity modeled
- `CompletedChapterRepository` created with Spring Data JPA
- Repository tests proving save, read, delete, and distinct chapter persistence

Not implemented yet:

- REST controllers
- Tracker service layer
- React frontend
- ESV chapter display endpoint

## Endpoint Documentation

No REST endpoints are currently implemented.

Planned V1 backend endpoints:

| Method | Path | Purpose | Status |
|---|---|---|---|
| `GET` | `/api/tracker` | Return the Bible catalog, completed chapters, and summary data for rendering the tracker | Planned |
| `PUT` | `/api/completions/{bookCode}/{chapterNumber}` | Mark a chapter complete | Planned |
| `DELETE` | `/api/completions/{bookCode}/{chapterNumber}` | Mark a chapter incomplete | Planned |
| `GET` | `/api/passages/{bookCode}/{chapterNumber}` | Return ESV passage text for a chapter | Planned |

Planned tracker response shape:

```json
{
  "summary": {
    "completedChapters": 5,
    "totalChapters": 1189,
    "completedBooks": 0,
    "totalBooks": 66
  },
  "books": [
    {
      "code": "GEN",
      "name": "Genesis",
      "testament": "OLD",
      "chapterCount": 50,
      "completedChapters": [1, 2, 3, 4, 50]
    }
  ]
}
```

## Design Notes

- The backend owns the Bible catalog and persistence rules.
- Stable book codes, such as `GEN` and `JHN`, are used in URLs and database rows.
- A completed chapter is represented by a row in the `completed_chapter` table.
- No row means the chapter is incomplete.
- The React client should never receive the ESV API key.
