package com.oncomm.oncomm.exception;

public class JsonParsingException extends RuntimeException {
    public JsonParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}