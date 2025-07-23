package com.oncomm.oncomm.exception;

// com.oncomm.oncomm.exception

public class CsvParsingException extends RuntimeException {
    public CsvParsingException(String message) {
        super(message);
    }
    public CsvParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}

