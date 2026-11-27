package com.krillion.question;

import java.util.Arrays;

/**
 * What kind of TMDB entity an answer is. The database stores the lowercase value
 * ("movie", "tv", "person"), because TMDB uses the same words and ids can overlap between types.
 */
public enum TmdbType {
    MOVIE("movie"),
    TV("tv"),
    PERSON("person");

    private final String value;

    TmdbType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static TmdbType fromValue(String value) {
        return Arrays.stream(values())
                .filter(type -> type.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown media type: " + value));
    }
}
