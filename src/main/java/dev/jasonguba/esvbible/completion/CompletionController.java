package dev.jasonguba.esvbible.completion;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/completions")
public class CompletionController {

    private final CompletedChapterService completedChapterService;

    public CompletionController(CompletedChapterService completedChapterService) {
        this.completedChapterService = completedChapterService;
    }

    @PutMapping("/{bookCode}/{chapterNumber}")
    public void markComplete(
        @PathVariable String bookCode, 
        @PathVariable int chapterNumber
    ) {
        completedChapterService.markComplete(bookCode, chapterNumber);
    }

    @DeleteMapping("/{bookCode}/{chapterNumber}")
    public void markIncomplete(
        @PathVariable String bookCode, 
        @PathVariable int chapterNumber
    ) {
        completedChapterService.markIncomplete(bookCode, chapterNumber);
    }
}
