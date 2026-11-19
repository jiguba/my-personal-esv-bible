import type { TrackerBook } from "./types";

type ChapterLocation = { book: TrackerBook; chapter: number } | undefined;

export function findPreviousChapter(books: TrackerBook[], bookCode: string, chapter: number): ChapterLocation {
    const index = books.findIndex((book) => book.code === bookCode);
    if (index === -1) {
        return undefined;
    }

    const currentBook = books[index];

    if (chapter < 1 || chapter > currentBook.chapterCount) {
        return undefined;
    }

    if (chapter === 1) {
        const prevBook = books[index - 1];

        if (prevBook === undefined) {
            return undefined;
        }

        return { book: prevBook, chapter: prevBook.chapterCount };
    }

    return { book: currentBook, chapter: chapter - 1 };
}

export function findNextChapter(books: TrackerBook[], bookCode: string, chapter: number): ChapterLocation {
    const index = books.findIndex((book) => book.code === bookCode);
    if (index === -1) {
        return undefined;
    }

    const currentBook = books[index];

    if (chapter < 1 || chapter > currentBook.chapterCount) {
        return undefined;
    }

    if (chapter === currentBook.chapterCount) {
        const nextBook = books[index + 1];

        if (nextBook === undefined) {
            return undefined;
        }

        return { book: nextBook, chapter: 1 };
    }

    return { book: currentBook, chapter: chapter + 1 };
}

