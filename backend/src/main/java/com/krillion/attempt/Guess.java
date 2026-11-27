package com.krillion.attempt;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

/**
 * One distinct guess inside an attempt. Rows are written with GuessRepository.insertIfAbsent,
 * so the same guess twice is ignored; this class is only used for reading.
 */
@Entity
@Table(name = "guess")
public class Guess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "attempt_id", nullable = false)
    private Long attemptId;

    @Column(name = "media_type", nullable = false, length = 10)
    private String mediaType;

    @Column(name = "tmdb_id", nullable = false)
    private Long tmdbId;

    @Column(name = "is_valid", nullable = false)
    private boolean valid;

    // filled by the database default (now())
    @Column(name = "guessed_at", insertable = false, updatable = false)
    private Instant guessedAt;

    protected Guess() {
    }

    public Long getId() {
        return id;
    }

    public Long getAttemptId() {
        return attemptId;
    }

    public String getMediaType() {
        return mediaType;
    }

    public Long getTmdbId() {
        return tmdbId;
    }

    public boolean isValid() {
        return valid;
    }

    public Instant getGuessedAt() {
        return guessedAt;
    }
}
