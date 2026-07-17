package dev.jasonguba.esvbible.completion;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class CompletedChapterId implements Serializable {
    
    // Maps the Java field bookCode to the database column book_code.
    // This is part of the composite primary key for CompletedChapter.
    @Column(name = "book_code", nullable = false)
    private String bookCode;

    // Maps the Java field chapterNumber to the database column chapter_number.
    // This is the second part of the composite primary key.
    @Column(name = "chapter_number", nullable = false)
    private int chapterNumber;

    // JPA requires a no-argument constructor so it can create this object
    // when reading rows from the database.
    protected CompletedChapterId() {
        // Required by JPA
    }

    // Application code uses this constructor to create a meaningful ID.
    public CompletedChapterId(String bookCode, int chapterNumber) {
        this.bookCode = bookCode;
        this.chapterNumber = chapterNumber;
    }

    public String getBookCode() {
        return bookCode;
    }

    public int getChapterNumber() {
        return chapterNumber;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof CompletedChapterId that)) {
            return false;
        }

        return chapterNumber == that.chapterNumber
            && Objects.equals(bookCode, that.bookCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookCode, chapterNumber);
    }


}

