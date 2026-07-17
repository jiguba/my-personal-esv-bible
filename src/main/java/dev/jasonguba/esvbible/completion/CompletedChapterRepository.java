package dev.jasonguba.esvbible.completion;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CompletedChapterRepository extends JpaRepository<CompletedChapter, CompletedChapterId> {

}
