package com.krillion.common;

/** The JSON body of every error response we create ourselves. */
public record ApiError(String code, String message) {
}
