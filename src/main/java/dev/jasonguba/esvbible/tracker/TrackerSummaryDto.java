package dev.jasonguba.esvbible.tracker;

public class TrackerSummaryDto {
    private final int completedChapters;
    private final int totalChapters;
    private final int completedBooks;
    private final int totalBooks;

    public TrackerSummaryDto(
        int completedChapters,
        int totalChapters,
        int completedBooks,
        int totalBooks
    ) {
        this.completedChapters = completedChapters;
        this.totalChapters = totalChapters;
        this.completedBooks = completedBooks;
        this.totalBooks = totalBooks;
    }

    public int getCompletedChapters() {
        return completedChapters;
    }

    public int getTotalChapters() {
        return totalChapters;
    }

    public int getCompletedBooks() {
        return completedBooks;
    }

    public int getTotalBooks() {
        return totalBooks;
    }
}






