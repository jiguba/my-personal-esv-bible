import { useState, useEffect } from "react";
import { BookRow } from "./components/BookRow";
import type { TrackerResponse } from "./types";

function App() {
  const [tracker, setTracker] = useState<TrackerResponse | null>(null);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    fetch("/api/tracker")
      .then((response) => {
        if (!response.ok) {
          throw new Error(`HTTP ${response.status}`);
        }
        return response.json();
      })
      .then((data: TrackerResponse) => setTracker(data))
      .catch((err) => setError(err.message));
  }, []);

  if (error !== null) return <p>Could not load tracker: {error}</p>;
  if (tracker === null) return <p>Loading…</p>;

  return (
    <main className="page">
      <div>
        <h1>My Personal ESV Bible Tracker</h1>
        <p>Let's begin reading the Bible</p>
        <p>{tracker.summary.completedChapters} of {tracker.summary.totalChapters} chapters read.</p>
        <p>{tracker.summary.completedBooks} of {tracker.summary.totalBooks} books completed.</p>
      </div>
      <ul className="book-list">
        {tracker.books.map((book) => (
          <BookRow key={book.code} book={book} />
        ))}
      </ul>
    </main>
  );
}

export default App;
