package com.java.api.exception;

public class NotFoundException extends RuntimeException {
    public static final int HTTP_STATUS = 404;

    public NotFoundException(String message) {
        super(message);
    }
}
