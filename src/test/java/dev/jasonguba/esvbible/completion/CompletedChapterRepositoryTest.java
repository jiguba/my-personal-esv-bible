package dev.jasonguba.esvbible.completion;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:sqlite:target/test-esv-bible.db",
    "spring.datasource.driver-class-name=org.sqlite.JDBC",
    "spring.jpa.database-platform=org.hibernate.community.dialect.SQLiteDialect",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
public class CompletedChapterRepositoryTest {

    @Autowired
    private CompletedChapterRepository repository;

    @Test
    void saveCompletedChapterThenFindsItById() {
        CompletedChapterId id = new CompletedChapterId("JHN", 3);
        Instant completedAt = Instant.parse("2026-07-16T12:00:00Z");

        CompletedChapter completedChapter = new CompletedChapter(id, completedAt);

        repository.save(completedChapter);

        Optional<CompletedChapter> result = repository.findById(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
        assertEquals(completedAt, result.get().getCompletedAt());
    }

    @Test
    void deleteCompletedChapterRemovesItFromTheDatabase() {
        CompletedChapterId id = new CompletedChapterId("GEN", 1);
        CompletedChapter completedChapter = new CompletedChapter(id, Instant.now());

        repository.save(completedChapter);

        repository.deleteById(id);

        assertFalse(repository.existsById(id));
    }

    @Test
    void differentChaptersInSameBookAreDifferentRows() {
        CompletedChapter john3 = new CompletedChapter(
            new CompletedChapterId("JHN", 3),
            Instant.now()
        );

        CompletedChapter john4 = new CompletedChapter(
            new CompletedChapterId("JHN", 4),
            Instant.now()
        );

        repository.save(john3);
        repository.save(john4);

        assertEquals(2, repository.count());
    }

}
