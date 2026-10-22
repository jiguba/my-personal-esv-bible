package dev.jasonguba.esvbible.completion;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import dev.jasonguba.esvbible.models.BibleBook;

@Service
public class CompletedChapterService {

    private final CompletedChapterRepository repository;

    public CompletedChapterService(CompletedChapterRepository repository) {
        this.repository = repository;
    }

    public void markComplete(String bookCode, int chapterNumber) {
        CompletedChapterId id = validateAndCreateId(bookCode, chapterNumber);

        if (repository.existsById(id)) {
            // Chapter is already marked as completed, no action needed
            return;
        }

        CompletedChapter completedChapter = new CompletedChapter(id, Instant.now());

        repository.save(completedChapter);
    }

    public void markIncomplete(String bookCode, int chapterNumber) {
        CompletedChapterId id = validateAndCreateId(bookCode, chapterNumber);

        if (!repository.existsById(id)) {
            // Chapter is not marked as completed, no action needed
            return;
        }

        repository.deleteById(id);
    }

    private CompletedChapterId validateAndCreateId(String bookCode, int chapterNumber) {
        Optional<BibleBook> maybeBook = BibleBook.findByCode(bookCode);

        if (maybeBook.isEmpty()) {
            throw new UnknownChapterException("Unknown Bible book code: " + bookCode);
        }

        BibleBook bibleBook = maybeBook.get();

        if (!bibleBook.containsChapter(chapterNumber)) {
            throw new UnknownChapterException(
                "Unknown chapter number " + chapterNumber 
                + " for book " + 
                bibleBook.getDisplayName());
        }

        return new CompletedChapterId(bibleBook.getCode(), chapterNumber);
    }

    public List<CompletedChapter> findAllCompletedChapters() {
        return repository.findAll();
    }
}
