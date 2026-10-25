package dev.jasonguba.esvbible.passage;

import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.jasonguba.esvbible.clients.ESVCrosswayClient;
import dev.jasonguba.esvbible.models.BibleBook;

@RestController
@RequestMapping("/api/passages")
public class PassageController {

    private final ESVCrosswayClient esvCrosswayClient;

    public PassageController(ESVCrosswayClient esvCrosswayClient) {
        this.esvCrosswayClient = esvCrosswayClient;
    }

    @GetMapping("/{bookCode}/{chapterNumber}")
    public String getChapterText(
        @PathVariable String bookCode,
        @PathVariable int chapterNumber
    ) {
        Optional<BibleBook> maybeBook = BibleBook.findByCode(bookCode);

        BibleBook bibleBook = maybeBook.orElseThrow();

        return esvCrosswayClient.getChapterText(bibleBook, chapterNumber);
    }

}
