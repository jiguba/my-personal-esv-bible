package dev.jasonguba.esvbible.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import dev.jasonguba.esvbible.completion.CompletedChapter;
import dev.jasonguba.esvbible.completion.CompletedChapterId;
import dev.jasonguba.esvbible.completion.CompletedChapterService;
import dev.jasonguba.esvbible.models.BibleBook;

@Service
public class TrackerService {
    private final CompletedChapterService completedChapterService;

    public TrackerService(CompletedChapterService completedChapterService) {
        this.completedChapterService = completedChapterService;
    }

    public TrackerResponseDto getTracker() {
        List<TrackerBookDto> books = new ArrayList<>();
        Map<String, List<Integer>> completedChaptersByBook = new HashMap<>();

        List<CompletedChapter> completedChapters = completedChapterService.findAllCompletedChapters();

        for (CompletedChapter completedChapter : completedChapters) {
            CompletedChapterId id = completedChapter.getId();

            completedChaptersByBook.computeIfAbsent(
                id.getBookCode(), k -> new ArrayList<>())
                .add(id.getChapterNumber());
        }

        for (BibleBook book : BibleBook.values()) {
            books.add(new TrackerBookDto(
                book.getCode(), 
                book.getDisplayName(),
                book.getTestament(),
                book.getNumberOfChapters(),
                completedChaptersByBook.getOrDefault(book.getCode(), List.of())
            ));
        }

        int completedChapterCount = completedChapters.size();

        int totalChapterCount = 0;
        int completedBookCount = 0;

        for (BibleBook book : BibleBook.values()) {
            totalChapterCount += book.getNumberOfChapters();

            List<Integer> completedChaptersForBook = 
                completedChaptersByBook.getOrDefault(book.getCode(), List.of());

            if (completedChaptersForBook.size() == book.getNumberOfChapters()) {
                completedBookCount++;
            }
        }

        int totalBookCount = BibleBook.values().length;

        return new TrackerResponseDto(
            new TrackerSummaryDto(
                completedChapterCount, 
                totalChapterCount, 
                completedBookCount, 
                totalBookCount
            ),
            books
        );
    }
}
