package dev.jasonguba.esvbible.clients;

public class FailedPassageTextFetchException extends RuntimeException {
    public FailedPassageTextFetchException(String message, Throwable cause) {
        super(message, cause);
    }

    public FailedPassageTextFetchException(String message) {
        super(message);
    }
}
