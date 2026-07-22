package dev.jasonguba.esvbible.services;

import org.springframework.stereotype.Service;

import dev.jasonguba.esvbible.completion.CompletedChapterRepository;

@Service
public class CompletedChapterService {

    private final CompletedChapterRepository repository;

    public CompletedChapterService(CompletedChapterRepository repository) {
        this.repository = repository;
    }

}
