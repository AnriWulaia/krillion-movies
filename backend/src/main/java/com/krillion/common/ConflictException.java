package com.krillion.common;

/** Something already exists or the action clashes with the current state (HTTP 409). */
public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }
}
