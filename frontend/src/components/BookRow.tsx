import type { TrackerBook } from "../types";
import "./BookRow.css";

type BookRowProps = { book: TrackerBook };

export function BookRow({ book }: BookRowProps) {
    const chapters = Array.from({ length: book.chapterCount }, (_, i) => i + 1);

    return (
        <li className="book-section">
            <div className="book-info">
                {book.name}: {book.completedChapters.length}/{book.chapterCount}
            </div>
            <div className="chapter-grid">
                {chapters.map((chapter) => {
                    const isRead = book.completedChapters.includes(chapter);

                    return <span key={chapter} className={isRead ? "chapter-cell is-read" : "chapter-cell"}>
                        {isRead ? "✓" : ""}{chapter}
                    </span>
                })}
            </div>
        </li>
    );
}