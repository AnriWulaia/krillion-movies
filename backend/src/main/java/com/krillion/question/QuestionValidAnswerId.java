package com.krillion.question;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

/** Primary key of a valid answer: the question plus which TMDB entity it is. */
@Embeddable
public record QuestionValidAnswerId(
        @Column(name = "question_id") Long questionId,
        @Column(name = "media_type", length = 10) String mediaType,
        @Column(name = "tmdb_id") Long tmdbId) implements Serializable {
}
