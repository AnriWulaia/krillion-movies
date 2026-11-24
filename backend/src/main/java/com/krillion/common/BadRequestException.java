package com.krillion.common;

/** The request is invalid in a way bean validation cannot express (HTTP 400). */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
