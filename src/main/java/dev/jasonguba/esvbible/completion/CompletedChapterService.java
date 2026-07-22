package dev.jasonguba.esvbible.completion;

import org.springframework.stereotype.Service;

@Service
public class CompletedChapterService {

    private final CompletedChapterRepository repository;

    public CompletedChapterService(CompletedChapterRepository repository) {
        this.repository = repository;
    }

}
