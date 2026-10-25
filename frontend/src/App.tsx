import { useState, useEffect } from "react";
import { BookRow } from "./components/BookRow";
import { TestamentFilter } from "./components/TestamentFilter";
import type { TrackerResponse, TestamentFilterType } from "./types";

function App() {
  const [tracker, setTracker] = useState<TrackerResponse | null>(null);
  const [filter, setFilter] = useState<TestamentFilterType>("ALL");
  const [error, setError] = useState<string | null>(null);

  function loadTracker() {
    fetch("/api/tracker")
      .then((response) => {
        if (!response.ok) {
          throw new Error(`HTTP ${response.status}`);
        }
        return response.json();
      })
      .then((data: TrackerResponse) => setTracker(data))
      .catch((err) => setError(err.message));
  }

  function handleToggle(bookCode: string, chapter: number, checked: boolean) {
    const url = `/api/completions/${bookCode}/${chapter}`;
    const method = checked ? "PUT" : "DELETE";

    fetch(url, { method })
      .then((response) => {
        if (!response.ok) {
          throw new Error(`HTTP ${response.status}`);
        }

        loadTracker()
      })
      .catch((err) => alert(`Couldn't save ${bookCode} ${chapter}: ${err.message}`));
  }

  useEffect(() => {
    loadTracker();
  }, []);

  if (error !== null) return <p>Could not load tracker: {error}</p>;
  if (tracker === null) return <p>Loading…</p>;

  const visibleBooks = tracker.books.filter((book) => book.testament === filter || filter === "ALL");

  return (
    <main className="page">
      <div>
        <h1>My Personal ESV Bible Tracker</h1>
        <p>Let's begin reading the Bible</p>
        <p>{tracker.summary.completedChapters} of {tracker.summary.totalChapters} chapters read.</p>
        <p>{tracker.summary.completedBooks} of {tracker.summary.totalBooks} books completed.</p>
      </div>
      <TestamentFilter value={filter} onClick={setFilter}></TestamentFilter>
      <ul className="book-list">
        {visibleBooks.map((book) => (
          <BookRow key={book.code} book={book} onToggle={handleToggle} />
        ))}
      </ul>
    </main>
  );
}

export default App;
