import type { TrackerBook } from "../types";

type BookRowProps = { book: TrackerBook };

export function BookRow({ book }: BookRowProps) {
    const chapters = Array.from({ length: book.chapterCount }, (_, i) => i + 1);

    return (
        <li>
            {book.name}: {book.completedChapters.length}/{book.chapterCount}
            <br />
            {chapters.map((chapter) =>
                <span key={chapter}>
                    {chapter}{book.completedChapters.includes(chapter) ? "✓ " : " "}
                </span>
            )}
        </li>
    );
}