package dev.jasonguba.esvbible.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import dev.jasonguba.esvbible.models.UnknownChapterException;

@RestControllerAdvice 
public class ApiExceptionHandler {

    @ExceptionHandler({UnknownChapterException.class})
    public ResponseEntity<String> handleUnknownChapter(UnknownChapterException ex) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ex.getMessage());
    }
}
