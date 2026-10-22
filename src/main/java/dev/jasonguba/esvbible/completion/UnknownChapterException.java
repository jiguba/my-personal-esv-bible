package dev.jasonguba.esvbible.completion;

public class UnknownChapterException extends RuntimeException {
    public UnknownChapterException(String message) {
        super(message);
    }
}
