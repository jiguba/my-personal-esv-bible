import { useState, useEffect } from "react";

type TrackerBook = {
  code: string;
  name: string;
  testament: "OLD" | "NEW";
  chapterCount: number;
  completedChapters: number[]
}

type TrackerSummary = {
  completedChapters: number;
  totalChapters: number;
  completedBooks: number;
  totalBooks: number
}

type TrackerResponse = {
  summary: TrackerSummary;
  books: TrackerBook[]
}

function App() {
  const [tracker, setTrackerResponse] = useState<TrackerResponse | null>(null);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    fetch("/api/tracker")
      .then((response) => {
        if (!response.ok) {
          throw new Error(`HTTP ${response.status}`);
        }
        return response.json();
      })
      .then((data: TrackerResponse) => setTrackerResponse(data))
      .catch((err) => setError(err.message));
  }, []);

  if (error !== null) return <p>Could not load tracker: {error}</p>;
  if (tracker === null) return <p>Loading…</p>;

  return (
    <section>
      <div>
        <h1>My Personal ESV Bible Tracker</h1>
        <p>Let's begin reading the Bible</p>
        <p>{tracker.summary.completedChapters} of {tracker.summary.totalChapters} chapters read</p>
      </div>

    </section>
  )
}

export default App
