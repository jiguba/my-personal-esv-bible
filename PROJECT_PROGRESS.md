# Project Progress

Last updated: 2026-06-21

## Mentoring Agreement

- Codex acts as a software architecture and engineering mentor.
- Codex may review the repository read-only, explain tradeoffs, propose small tickets, and provide limited example snippets.
- Codex does not implement application code, rewrite files, apply code patches, or commit changes.
- Jason implements application code and tests; Codex reviews the result afterward.
- Guiding principles: scale only when necessary, and remember that simple is already complex enough.

## Product Vision

Replace a Google Sheets Bible-reading tracker with a clean personal application that displays the entire Protestant Bible canon, persists chapter completion, summarizes progress, and opens ESV passage text.

## Version 1 Scope

- Single user; Jason is the tester.
- Display all 66 books and 1,189 chapters on one scrollable tracker page.
- Filter by All, Old Testament, or New Testament.
- Use a separate completion checkbox and chapter-reading link.
- Allow checking and unchecking any chapter.
- Persist completion state across refreshes and application restarts.
- Show chapters read and books completed.
- Retrieve ESV chapter text after the tracker is working.
- Keep tracking usable when the ESV API is unavailable.

## Out of Scope for Version 1

- Authentication and multiple users
- Custom reading plans
- Notes and reflections
- Commentary
- Multiple translations
- Favorites and streaks

## Architecture Decisions

- Frontend: React with TypeScript.
- Backend: Java with Spring Boot REST APIs.
- Persistence: SQLite.
- Persistence API: Spring Data JPA with Hibernate, chosen primarily as a learning goal.
- Deployment: eventually a cloud platform; SQLite requires durable storage and a single application instance.
- The React client never receives the ESV API credential.
- Spring Boot owns ESV integration and tracker persistence.
- The immutable Bible catalog is represented by a Java enum and served to React by the backend.
- Stable book codes, rather than display names or enum ordinals, are used in URLs and persistence.

## Persistence Model

`completed_chapter`

- `book_code`: non-null text
- `chapter_number`: non-null integer
- `completed_at`: non-null UTC timestamp
- Composite primary key: (`book_code`, `chapter_number`)
- A row exists when a chapter is complete; no row means incomplete.
- Checking an incomplete chapter creates a row and timestamp.
- Repeating a check is a no-op and retains the timestamp.
- Unchecking deletes the row.
- Rechecking after deletion creates a new timestamp.
- `completed_at` means the most recent transition from incomplete to complete.
- The JPA entity will use an embeddable composite ID with value-based `equals` and `hashCode`.

## API Contract

- `GET /api/tracker` returns the complete catalog, completion state, and summary.
- `PUT /api/completions/{bookCode}/{chapterNumber}` ensures a completion exists.
- `DELETE /api/completions/{bookCode}/{chapterNumber}` ensures a completion does not exist.
- `GET /api/passages/{bookCode}/{chapterNumber}` retrieves ESV text.
- Malformed input returns `400 Bad Request`.
- A well-formed but nonexistent book/chapter returns `404 Not Found`.
- Completion operations are idempotent.

Proposed tracker response:

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

Completion timestamps are intentionally omitted from this V1 response.

## ESV Integration Spike

- The ESV client successfully returned John 3:16.
- `ESV_CROSSWAY_API_KEY` is supplied through the environment.
- Local Git history contains the environment placeholder, not a literal credential.
- The temporary `CommandLineRunner` should be removed because startup must not depend on ESV availability.

## Repository Notes

- The Spring Boot backend currently lives at the repository root.
- The Maven wrapper script exists, but `.mvn/wrapper/maven-wrapper.properties` is missing; system `mvn` currently works.
- A React frontend has not been created yet.

## Current Ticket: Bible Catalog

Implement a `BibleBook` enum and focused unit tests before adding JPA or SQLite.

Acceptance criteria:

- Contains all 66 books in canonical order.
- Each book has a stable code, display name, testament, and chapter count.
- Supports lookup by stable code.
- Validates whether a chapter exists.
- Rejects zero, negative, above-maximum, and unknown chapters/books.
- Tests include `GEN/50`, `GEN/51`, `JHN/1`, `JHN/21`, and `JHN/22`.
- Catalog integrity tests verify 66 books, 1,189 chapters, unique codes, and correct testament totals.

## Next Review

After Jason implements the catalog and its tests, Codex will review them read-only. JPA and SQLite setup comes afterward.
