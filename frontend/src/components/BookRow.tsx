import type { TrackerBook } from "../types";
import "./BookRow.css"

type BookRowProps = { book: TrackerBook };

export function BookRow({ book }: BookRowProps) {
    const chapters = Array.from({ length: book.chapterCount }, (_, i) => i + 1);

    return (
        <li className="book-section">
            <div className="book-info">
                {book.name}: {book.completedChapters.length}/{book.chapterCount}
            </div>
            <div className="chapter-grid">
                {chapters.map((chapter) =>
                    <span key={chapter} className="chapter-cell">
                        {chapter}{book.completedChapters.includes(chapter) ? "✓" : ""}
                    </span>
                )}
            </div>
        </li>
    );
}