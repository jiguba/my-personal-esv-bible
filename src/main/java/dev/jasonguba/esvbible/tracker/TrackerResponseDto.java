package dev.jasonguba.esvbible.tracker;

import java.util.List;

public class TrackerResponseDto {

    private final TrackerSummaryDto summary;
    private final List<TrackerBookDto> books;

    public TrackerResponseDto(TrackerSummaryDto summary, List<TrackerBookDto> books) {
        this.summary = summary;
        this.books = books;
    }

    public TrackerSummaryDto getSummary() {
        return summary;
    }

    public List<TrackerBookDto> getBooks() {
        return books;
    }
}
