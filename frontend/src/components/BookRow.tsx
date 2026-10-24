import type { TrackerBook } from "../types";
import "./BookRow.css";

type BookRowProps = {
    book: TrackerBook;
    onToggle: (bookCode: string, chapter: number, checked: boolean) => void;
};

export function BookRow({ book, onToggle }: BookRowProps) {
    const chapters = Array.from({ length: book.chapterCount }, (_, i) => i + 1);

    return (
        <li className="book-section">
            <div className="book-info">
                {book.name}: {book.completedChapters.length}/{book.chapterCount}
            </div>
            <div className="chapter-grid">
                {chapters.map((chapter) => {
                    const isRead = book.completedChapters.includes(chapter);

                    return <label key={chapter} className={isRead ? "chapter-cell is-read" : "chapter-cell"}>
                        <input
                            type="checkbox"
                            checked={isRead}
                            onChange={(event) => onToggle(book.code, chapter, event.currentTarget.checked)}
                            aria-label={`Mark ${book.name} ${chapter} as read`}
                        />
                        {isRead ? "✓" : ""} {chapter}
                    </label>
                })}
            </div>
        </li>
    );
}