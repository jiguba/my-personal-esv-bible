# Project Progress

Last updated: 2026-10-28

## Mentoring Agreement

- Claude acts as a senior software engineering mentor, instructor and code reviewer (see `CLAUDE.md`).
- Claude may inspect the repository read-only, explain concepts and tradeoffs, write tickets, give progressive hints, and review code afterward.
- Claude does not write application code, modify project files, or commit. Jason implements all code and tests.
- Guiding principles: scale only when necessary; simple is already complex enough.

## Product Vision

Replace a Google Sheets Bible-reading tracker with a personal application that displays the entire Protestant Bible canon, persists chapter completion, summarizes progress, and opens ESV passage text.

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

## Current Status

**The core tracker works end to end:** the React page loads all 66 books from Spring Boot, and clicking a chapter saves it to SQLite. The app can replace the Google Sheet for day-to-day tracking.

**The ESV passage backend is done:** `GET /api/passages/{bookCode}/{chapterNumber}` returns chapter text, 404 for unknown chapters, and 503 when ESV is unavailable or no key is configured. The frontend doesn't call it yet.

### Completed milestones

| Area | Milestone | Commit(s) |
|---|---|---|
| Backend | Bible catalog enum (`BibleBook`, `Testament`) with integrity tests | `35496cb` |
| Backend | JPA persistence: `CompletedChapter`, composite key, repository, repository tests | `2bb9670`, `c2cf036` |
| Backend | Services: `CompletedChapterService` (idempotent complete/incomplete, validation), `TrackerService` (read model + DTOs) | `211e524`–`bbb5321` |
| Backend | REST endpoints: `GET /api/tracker`, `PUT`/`DELETE /api/completions/{bookCode}/{chapterNumber}` | `2ba6126` |
| Backend | `UnknownChapterException` mapped to 404 by `ApiExceptionHandler` (`web` package) | `5d23ad9` |
| Backend | Book codes switched to USFM (`GEN`, `EXO`, `JHN`, ...) with format tests | `a1ce89e` |
| Frontend | React + TypeScript project with Vite in `frontend/` | `d4c075b` |
| Frontend | Vite dev proxy: `/api` → `localhost:8080` | `f1a9a4c` |
| Frontend | Tracker summary loaded from `/api/tracker` with loading and error states | `57ec048` |
| Frontend | Book list with `BookRow` component and shared types (`types.ts`) | `b4b4081` |
| Design | V1 design spec (`docs/DESIGN.md`) and reference mockups | `dffd919`, `1783fd8` |
| Frontend | Design tokens and fonts, page column, BookSection layout, chapter grid, read/unread cell states, automatic dark mode | `6b6f0a9`–`15d0458` |
| Frontend | Click or keyboard to toggle a chapter: `PUT`/`DELETE`, then refetch; visually hidden checkbox with focus ring | `b2850a1` |
| Frontend | Testament filter: All / Old / New segmented control (`aria-pressed`), state in `App`, client-side `.filter()` | `a65b4ad` |
| Backend | `GET /api/passages/{bookCode}/{chapterNumber}` fetches chapter text through `ESVCrosswayClient` | `e73569d` |
| Backend | Shared chapter validation in `BibleBook.requireValidChapter` (unknown book or chapter → 404), with `BibleBookTest` | `b55d90a` |
| Backend | ESV failures (HTTP error or no network) become `FailedPassageTextFetchException` (cause chained), mapped to 503 and logged | `a2ad893` |
| Backend | ESV key optional at startup: empty default, startup warning, short-circuit to 503 before any HTTP call, `ESVCrosswayClientTest` | pending commit |

### Next ticket

- **Frontend reading view:** open a chapter, fetch `/api/passages/{bookCode}/{chapterNumber}`, and show the text, with loading, success and failure (404 and 503) states.

### Remaining V1 work (rough order)

1. Reading view in the frontend: router and `PassageView`
2. SummaryHeader with progress bars and OT/NT stats (DESIGN.md §2.1); the summary is currently shown as plain text in `App`
3. Polish: read link per chapter, toast instead of `alert()`, BookIndex, phone layout, loading skeletons
4. `PassageControllerTest` (`@WebMvcTest`, including client throws → 503), deliberately saved for last

## Architecture Decisions

- Frontend: React with TypeScript, built with Vite, in `frontend/`.
- Backend: Java 17, Spring Boot REST APIs, at the repository root.
- Persistence: SQLite through Spring Data JPA with Hibernate (chosen primarily as a learning goal).
- Packages are grouped by feature (`completion`, `tracker`, `models`, `clients`), plus a cross-cutting `web` package for API-wide concerns.
- The immutable Bible catalog is a Java enum, served to React by the backend.
- **Book codes are USFM standard** (3 characters: `GEN`, `EXO`, `JHN`, `PHM`, ...). They're used in URLs and persistence; never use display names or enum ordinals.
- Services throw domain exceptions; `ApiExceptionHandler` (`@RestControllerAdvice`) translates them to HTTP statuses. Services know nothing about HTTP.
- Error responses contain only messages written by the application, never internal exception details.
- In development, Vite proxies `/api` to Spring Boot, so the browser makes same-origin requests (no CORS configuration).
- `App` owns the tracker state. After every save it refetches `/api/tracker`, so the backend stays the single source of truth for counts.
- The testament filter is client-side only. `App` owns the filter state; `TestamentFilter` is a controlled component (value down, change callback up).
- Styling uses CSS custom properties from `frontend/src/styles/tokens.css` only. No hard-coded colors; dark mode follows the OS.
- The React client never receives the ESV API credential.
- ESV is an optional dependency, so the app degrades gracefully instead of failing fast: a missing key or ESV outage affects only passages (503), never the tracker.
- External-service errors are translated at the client boundary: `ESVCrosswayClient` wraps `RestClientException` in `FailedPassageTextFetchException` and keeps the original as the cause for logging.
- Deployment (eventually): a cloud platform; SQLite requires durable storage and a single application instance.

## Persistence Model

`completed_chapter`

- `book_code`: non-null text (USFM code)
- `chapter_number`: non-null integer
- `completed_at`: non-null UTC timestamp
- Composite primary key: (`book_code`, `chapter_number`)
- A row exists when a chapter is complete; no row means incomplete.
- Checking an incomplete chapter creates a row and timestamp.
- Repeating a check is a no-op and keeps the timestamp.
- Unchecking deletes the row.
- Rechecking after deletion creates a new timestamp.
- `completed_at` means the most recent change from incomplete to complete.
- Renaming stored values (like book codes) requires a data migration once real data exists.

## API Contract

- `GET /api/tracker` returns the complete catalog, completion state, and summary.
- `PUT /api/completions/{bookCode}/{chapterNumber}` ensures a completion exists (200).
- `DELETE /api/completions/{bookCode}/{chapterNumber}` ensures a completion does not exist (200).
- `GET /api/passages/{bookCode}/{chapterNumber}` retrieves ESV text (200).
- Malformed input (e.g. a non-numeric chapter) returns `400 Bad Request` (Spring default).
- An unknown book or out-of-range chapter returns `404 Not Found` with a plain-text message.
- When ESV is unavailable or no key is configured, the passage endpoint returns `503 Service Unavailable` with a plain-text message; tracker and completion endpoints are unaffected.
- Completion operations are idempotent.

Tracker response:

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

## Running Locally

1. Backend (repo root): `mvn spring-boot:run` → `localhost:8080`. Set `ESV_CROSSWAY_API_KEY` to read passages; without it the app starts, logs a warning, and passages return 503.
2. Frontend: `cd frontend && npm install && npm run dev` → `localhost:5173`
3. Tests: `mvn test` (backend), `npm run build` (frontend type check + build)

## Known Issues and Technical Debt

- `target/classes/application.properties` is still tracked by git (committed before `target/` was ignored).
- No unit tests yet for `CompletedChapterService` or `TrackerService`, and no web-layer tests for the controllers.
- `ESVCrosswayClientTest` shows ESV wasn't called only indirectly (null cause); if the guard were removed, the test would make a real network call. `MockRestServiceServer` could prove "no request" directly.
- The "every code is 3 characters" test message reads "Expected 3 Amoss..." and should name the book code clearly.
- A failed save uses `alert()`, which shows the book code rather than the name.
- ESLint warns about `loadTracker` missing from the `useEffect` dependency array (fix later with `useCallback`).
- `TestamentFilter`'s callback prop is named `onClick`; rename it to `onChange` (DESIGN.md §5) because it reports a value change, not a raw click.
- `TestamentFilter.css` uses a hard-coded shadow color (`rgba(0, 0, 0, 0.12)`), which breaks the "tokens only" rule; add a `--shadow` token (with a dark-mode value) or record the exception.
- `docs/implementation-milestone-tracker.md` and `docs/progress-study-guide.md` are outdated.
- The Maven wrapper is missing `.mvn/wrapper/maven-wrapper.properties`; system `mvn` works.

## Future Improvements (beyond the current tickets)

- Reusable `LoadingState` and `ErrorNotice` components with a Retry button, once a second screen needs them.
- Optimistic updates with a saving state and toast (DESIGN.md §4).
- Database migrations with Flyway or Liquibase.
- `React.memo` for `BookSection`, only if measurement shows a performance problem.
- Prettier for consistent formatting.
