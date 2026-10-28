package dev.jasonguba.esvbible.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import dev.jasonguba.esvbible.clients.FailedPassageTextFetchException;
import dev.jasonguba.esvbible.models.UnknownChapterException;

@RestControllerAdvice 
public class ApiExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler({UnknownChapterException.class})
    public ResponseEntity<String> handleUnknownChapter(UnknownChapterException ex) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ex.getMessage());
    }

    @ExceptionHandler({FailedPassageTextFetchException.class})
    public ResponseEntity<String> handleFailedPassageTextFetch(FailedPassageTextFetchException ex) {
        logger.error(ex.getMessage(), ex);
        return ResponseEntity
            .status(HttpStatus.SERVICE_UNAVAILABLE)
            .body("Couldn't load this chapter right now. Please try again in a few minutes.");
    }
}
