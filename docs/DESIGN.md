# My Personal ESV Bible: Design Spec (V1)

This is the design source of truth for the React + TypeScript (Vite) frontend.

- **Tokens:** `frontend/src/styles/tokens.css`. Import it once in `main.tsx`. Use the CSS variables; don't hard-code colors or sizes in components.
- **Reference mockups:** `design/reference/*.dc.html`. These are the canvas source files. They don't run on their own, but they hold the exact markup, sizes and inline styles for every screen. Search them when you need a precise value.
- **Screenshots:** `design/screenshots/` (export these from the design canvas: Share › Export, one image per board).

Scope follows `PROJECT_PROGRESS.md`. Nothing here adds accounts, plans, streaks, notes or completion dates.

---

## 1. Look and feel

Calm and book-like: paper-toned background, serif for headings and scripture, clean sans for every control. One accent color (`--done`, deep navy) marks reading progress. Completion is always shown with **fill plus a checkmark**, never color alone. Light theme by default; dark theme follows the OS.

| Role | Font | Notes |
|---|---|---|
| Headings, book names, big numbers, scripture | Newsreader | weight 500 |
| Everything else (controls, counts, labels) | IBM Plex Sans | 400 / 500 / 600 |
| Numbers in counts and stats | either | `font-variant-numeric: tabular-nums` |

---

## 2. Screens

### 2.1 Tracker page (desktop board: "Tracker — desktop")

Centered column, `max-width: var(--page-max)`, horizontal padding `var(--page-pad)`. Top to bottom:

1. **Header.** Book icon + "My Personal ESV Bible" (Newsreader 20/600) on the left; "Progress saves as you check" (13px, muted) on the right.
2. **SummaryHeader.**
   - Uppercase label "Reading progress" (12px, 600, letter-spacing 0.12em, muted).
   - Big number: `completedChapters` (Newsreader 88px) + "of 1,189 chapters read" (18px muted).
   - Full-width progress bar: 10px tall, pill radius, `--track` background, `--done` fill, percentage to one decimal on the right.
   - Three stats to the right (wraps below on narrow screens), each with a 1px `--line` left border: **Books complete** (`completedBooks` / 66), **Old Testament** (x / 929, with a 4px mini bar), **New Testament** (x / 260, with a 4px mini bar).
3. **TestamentFilter** (sticky at top while scrolling, `--bg` background, `--line` borders top and bottom).
   - Segmented control in a `--chip` group: **All 1,189 · Old Testament 929 · New Testament 260**. Active segment uses `--surface` with a small shadow. Each segment is a `<button aria-pressed>`, min height 40px.
   - Right side: a scope line, e.g. "Showing all 66 books" or "Showing Old Testament · 275 of 929 chapters read".
4. **BookIndex** ("Jump to a book"). One row per visible testament: label ("Old Testament") then one 58×44 chip per book showing the book code and a 3px progress bar. Chip links to `#book-{code}`. Completed books get a filled `--done` chip.
5. **Book list.** For each visible testament, an H2 (Newsreader 36px) with a meta line ("39 books · 9 complete · 275 of 929 chapters"). Then one **BookSection** per book.

**BookSection** layout: a row with a 1px `--line` top border, 18px vertical padding.
- Left column, 220px: book name (Newsreader 22px), "12 / 50" count, a "Complete" pill when every chapter is read, a 4px progress bar.
- Right: the chapter grid, `display: grid; grid-template-columns: repeat(auto-fill, 72px); gap: 6px`.
- `id="book-{code}"` and `scroll-margin-top: 80px` so the sticky filter doesn't cover it.

### 2.2 Tracker on a phone (board: "Tracker — phone", 390px wide)

- Page padding 20px. Summary shrinks: number 60px, books complete shown as one line under the bar. OT/NT stats are dropped.
- Filter is a 3-column segmented control (**All / Old / New**), 44px tall.
- BookIndex becomes a "Jump to" `<select>`.
- Each book is an accordion row: `<button aria-expanded>` with name, count, check icon if complete, 3px bar, and a chevron.
- Chapter grid is 3 columns; cells are 44px tall with a 44px read link.
- *Optional:* completed books start collapsed to shorten the scroll. This goes slightly beyond V1; skip it if you want every book expanded.

### 2.3 Reading view (board: "Reading view — desktop")

Separate route, e.g. `/read/:bookCode/:chapter`.
- Top bar: "‹ Back to tracker" link (returns to `/#book-{code}`), app name on the right.
- Content column `max-width: var(--reading-max)`, 56px top padding.
- Label "New Testament · John · 3 of 21", then H1 "John 3" (Newsreader 56px).
- Action row between two `--line` rules: a 24px checkbox labeled "Mark John 3 as read" (when checked: "Read — John 3 is marked complete"), and "‹ John 2 / John 4 ›" links on the right.
- Passage text from `GET /api/passages/{bookCode}/{chapter}`: Newsreader 20px, line-height 1.7, verse numbers as small sans superscripts in `--accent`.
- Crossway copyright notice under the text (12px muted). Required by the ESV API terms.
- Large Previous / Next cards at the bottom (two columns, 72px tall, `--surface`).
- Previous/next crosses book boundaries (Malachi 4 → Matthew 1). Hide Previous on Genesis 1 and Next on Revelation 22.

---

## 3. ChapterCell (the most important component)

Every chapter has **two separate controls** in one cell:

```
┌──────────────┬────┐
│ ✓  12        │ 📖 │   72 × 34 (desktop) · 3-column grid × 44 (phone)
└──────────────┴────┘
  checkbox       read link
```

| Part | Element | Accessible name | Behavior |
|---|---|---|---|
| Left (number + check) | native `<input type="checkbox">`, visually hidden, covering the left area, inside a `<label>` | "Mark {Book} {n} as read" | toggles completion |
| Right (book icon) | `<a href="/read/{code}/{n}">` | "Read {Book} {n}" | opens reading view |

Divider between the two parts: 1px `--line` (unread) or `--done-divider` (read).

**States**

| State | Background | Border | Text/icon | Extra |
|---|---|---|---|---|
| Unread | `--surface` | `--tile-line` | `--ink`, read icon `--muted` | — |
| Read | `--done` | `--done` | `--on-done` | 10px checkmark before the number |
| Keyboard focus | — | — | — | 2px `--focus` ring inside the checkbox area |
| Saving | as "Read" | — | — | `opacity: 0.7` until the request returns |
| Couldn't save | as "Unread" (reverted) | 1.5px `--accent` | — | outline stays until the toast closes |

Hover on the read link: light gray background tint.

---

## 4. Behavior

**Toggling a chapter (optimistic):**
1. Update the UI immediately and mark the cell as saving.
2. Send `PUT /api/completions/{code}/{n}` (check) or `DELETE` (uncheck).
3. Success: clear the saving state. Summary, book counts and bars update from local state, so there's no need to refetch the whole tracker.
4. Failure: revert the cell, add the failed outline, show a toast: "Couldn't save Ruth 3, so it's still unread." with **Retry** and a dismiss button. The toast is `role="status"` and doesn't block anything else.

**Filter:** client-side only; hides the other testament's index row and book list. The summary always shows whole-Bible numbers.

**Loading the tracker:** skeleton blocks in `--chip` color where the number, bar and first books go, plus "Loading your progress…" (`role="status"`).

**Tracker failed to load:** amber info icon, "Couldn't load your tracker", "The server didn't respond. Your reading progress is saved and will appear once it reconnects.", primary **Try again** button (`role="alert"` on the container).

**ESV unavailable in the reading view:** keep the title and the mark-as-read checkbox. Replace the text with a panel: "The passage text isn't available right now. Your progress isn't affected. You can still mark John 3 as read." plus **Try again** and **Back to tracker**.

**Milestones (understated):**
- Book complete: navy "✓ Complete" pill next to the count.
- Whole Bible: number reads 1,189, bar full, plus one italic Newsreader line: "Every chapter, Genesis to Revelation." No confetti or modal.

**Fresh start (0 read):** "0 of 1,189 chapters read", empty bar, and "Check any chapter below to begin. Read in any order. The tracker simply remembers."

---

## 5. Components → API data

Types match `GET /api/tracker`:

```ts
type Testament = "OLD" | "NEW";

interface Summary {
  completedChapters: number;
  totalChapters: number;   // 1189
  completedBooks: number;
  totalBooks: number;      // 66
}

interface Book {
  code: string;            // "GEN"
  name: string;            // "Genesis"
  testament: Testament;
  chapterCount: number;
  completedChapters: number[];
}

interface TrackerResponse {
  summary: Summary;
  books: Book[];
}
```

| Component | Props (suggested) | Source |
|---|---|---|
| `SummaryHeader` | `summary`, `otCompleted`, `ntCompleted` | `summary`; OT/NT totals computed from `books` |
| `TestamentFilter` | `value: "ALL" \| Testament`, `onChange` | local state |
| `BookIndex` | `books`, `filter` | `books` |
| `BookSection` | `book`, `pending: Set<number>`, `failed: Set<number>`, `onToggle(code, n, checked)` | one `Book` |
| `ChapterCell` | `bookCode`, `bookName`, `chapter`, `done`, `status: "idle" \| "saving" \| "failed"`, `onToggle` | derived per chapter |
| `ProgressBar` | `value`, `max`, `size: "lg" \| "sm" \| "xs"`, `label` | — |
| `Toast` | `message`, `onRetry`, `onDismiss` | toggle failures |
| `PassageView` | `bookCode`, `chapter` | `GET /api/passages/...` |
| `ErrorNotice`, `LoadingState` | `title`, `message`, `onRetry` | fetch status |

Performance: 1,189 cells render on one page. Wrap `BookSection` in `React.memo` and keep each book's completion array stable so one toggle re-renders one book.

---

## 6. Accessibility checklist

- Text contrast 4.5:1 or better in both themes (the token pairs above pass).
- Every chapter checkbox and read link has its own accessible name (see section 3).
- Visible focus ring (`--focus`) on every interactive element.
- Touch targets 44px or more on phones. Desktop chapter cells are 34px tall, which meets the WCAG 2.2 AA 24px minimum.
- Progress bars use `role="progressbar"` with `aria-valuenow`, `aria-valuemin`, `aria-valuemax` and a label.
- The filter uses `aria-pressed`; the phone accordion uses `aria-expanded`.
- Respect `prefers-reduced-motion` (handled in `tokens.css`).

---

## 7. Open items

- **Book codes:** the mockups use USFM-style codes (GEN, EXO, ... EZK, JOL, JHN, PHM, JUD, REV). The frontend should use whatever `BibleBook` returns; check that the two match.
- **Crossway notice:** get the exact copyright wording from the ESV API terms before shipping the reading view.
