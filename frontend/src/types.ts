export type TrackerBook = {
    code: string;
    name: string;
    testament: "OLD" | "NEW";
    chapterCount: number;
    completedChapters: number[];
};

export type TrackerSummary = {
    completedChapters: number;
    totalChapters: number;
    completedBooks: number;
    totalBooks: number;
};

export type TrackerResponse = {
    summary: TrackerSummary;
    books: TrackerBook[];
};