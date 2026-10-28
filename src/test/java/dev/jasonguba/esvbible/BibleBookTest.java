package dev.jasonguba.esvbible;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import dev.jasonguba.esvbible.models.BibleBook;
import dev.jasonguba.esvbible.models.Testament;
import dev.jasonguba.esvbible.models.UnknownChapterException;

public class BibleBookTest {
    
    @Test
    void findByCodeReturnsMatchingBook() {
        var result = BibleBook.findByCode("GEN");

        assertTrue(result.isPresent());
        assertEquals(BibleBook.GENESIS, result.get());
    }


    @Test
    void findByCodeReturnsEmptyForUnknownCode() {
        var result = BibleBook.findByCode("DNE");

        assertTrue(result.isEmpty());
    }

    @Test
    void containsChapterNumberAcceptsValidChapter() {
        assertTrue(BibleBook.GENESIS.containsChapter(10));
        assertTrue(BibleBook.GENESIS.containsChapter(50));
    }

    @Test
    void containsChapterNumberRejectsInvalidChapter() {
        assertFalse(BibleBook.GENESIS.containsChapter(0));
        assertFalse(BibleBook.GENESIS.containsChapter(-10));
        assertFalse(BibleBook.GENESIS.containsChapter(51));
    }

    @Test
    void bibleBookContainsExpectedNumberOfBooks() {
        assertEquals(66, BibleBook.values().length);
    }

    @Test
    void bibleBookContainsExpectedTotalOfChapters() {
        int numberOfChapters = 0;

        for (BibleBook book: BibleBook.values()) {
            numberOfChapters += book.getNumberOfChapters();
        }

        assertEquals(1189, numberOfChapters);

    }

    @Test 
    void numberOfOldTestamentBooks() {
        int sumOldTestament = 0;
        
        for (BibleBook book: BibleBook.values()) {
            if (book.getTestament() == Testament.OLD) {
                sumOldTestament++;
            }
        }

        assertEquals(39, sumOldTestament);
    }

    @Test 
    void numberOfNewTestamentBooks() {
        int sumNewTestament = 0;
        
        for (BibleBook book: BibleBook.values()) {
            if (book.getTestament() == Testament.NEW) {
                sumNewTestament++;
            }
        }

        assertEquals(27, sumNewTestament);
    }

    @Test 
    void numberOfOldTestamentChapters() {
        int sumOldTestamentChapters = 0;
        
        for (BibleBook book: BibleBook.values()) {
            if (book.getTestament() == Testament.OLD) {
                sumOldTestamentChapters += book.getNumberOfChapters();
            }
        }

        assertEquals(929, sumOldTestamentChapters);
    }

    @Test 
    void numberOfNewTestamentChapters() {
        int sumNewTestamentChapters = 0;
        
        for (BibleBook book: BibleBook.values()) {
            if (book.getTestament() == Testament.NEW) {
                sumNewTestamentChapters += book.getNumberOfChapters();
            }
        }

        assertEquals(260, sumNewTestamentChapters);
    }

    @Test
    void allBookCodesAreUnique() {
        Set<String> codes = new HashSet<>();

        for (BibleBook book: BibleBook.values()) {
            codes.add(book.getCode());
        }

        assertEquals(BibleBook.values().length, codes.size());

    }

    @Test
    void allBooksHaveNonBlankCodes() {
        for (BibleBook book: BibleBook.values()) {
            String code = book.getCode();
            assertFalse(code.isBlank());
        }
    }

    @Test
    void allBooksHaveNonBlankDisplayNames() {
        for (BibleBook book: BibleBook.values()) {
            String displayName = book.getDisplayName();
            assertFalse(displayName.isBlank());
        }
    }

    @Test
    void allBooksHaveAtLeastOneChapter() {
        for (BibleBook book: BibleBook.values()) {
            assertTrue(book.getNumberOfChapters() >= 1);
        }
    }

    @Test 
    void containsChapterHandlesJohnBoundaries() {
        assertTrue(BibleBook.JOHN.containsChapter(1));
        assertTrue(BibleBook.JOHN.containsChapter(21));
        assertFalse(BibleBook.JOHN.containsChapter(0));
        assertFalse(BibleBook.JOHN.containsChapter(22));
    }

    @Test 
    void containsChapterHandlesPhilemonBoundaries() {
        assertTrue(BibleBook.PHILEMON.containsChapter(1));
        assertFalse(BibleBook.PHILEMON.containsChapter(0));
        assertFalse(BibleBook.PHILEMON.containsChapter(2));
    }

    @Test 
    void everyCodeHasExactlyThreeCharacters() {
        for (BibleBook book: BibleBook.values()) {
            int codeLength = book.getCode().length();
            String bookName = book.getDisplayName();
            assertEquals(3, codeLength, String.format("Expected 3 %ss but found %d", bookName, codeLength));
        }
    }

    @Test
    void findByCodeReturnsCorrectJohnBook() {
        var result = BibleBook.findByCode("JHN");

        assertTrue(result.isPresent());
        assertEquals(BibleBook.JOHN, result.get());
    }

    @Test
    void findByCodeReturnsEmptyForOldJohnCode() {
        var result = BibleBook.findByCode("JOHN");

        assertTrue(result.isEmpty());
    }

    @Test
    void requireValidChapterReturnsCorrectBook() {
        var result = BibleBook.requireValidChapter("JHN", 21);

        assertEquals(BibleBook.JOHN, result);
    }

    @Test
    void requireValidChapterThrowsForUnknownCode() {
        assertThrows(UnknownChapterException.class, () -> BibleBook.requireValidChapter("XYZ", 1));
    }

    @Test
    void requireValidChapterThrowsExceptionForChapter22() {
        assertThrows(UnknownChapterException.class, () -> BibleBook.requireValidChapter("JHN", 22));
    }

    @Test
    void requireValidChapterThrowsExceptionForChapterZero() {
        assertThrows(UnknownChapterException.class, () -> BibleBook.requireValidChapter("JHN", 0));
    }

    @Test
    void buildReferenceCombinesDisplayNameAndChapter() {
        var reference = BibleBook.ONEJOHN.buildReference(3);

        assertEquals("1 John 3", reference);
    }
}
