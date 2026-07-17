package dev.jasonguba.esvbible.completion;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "completed_chapter")
public class CompletedChapter {
    
    @EmbeddedId
    private CompletedChapterId id;

    @Column(name = "completed_at", nullable = false)
    private Instant completedAt;

    protected CompletedChapter() {}

    public CompletedChapter(CompletedChapterId id, Instant completedAt) {
        this.id = id;
        this.completedAt = completedAt;
    }

    public CompletedChapterId getId(){
        return this.id;
    }

    public Instant getCompletedAt() {
        return this.completedAt;
    }
}
