package com.krillion.scoring;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

// Primary key of the statistics row: which question and which answer.
@Embeddable
public record AnswerStatId(
        @Column(name = "question_id") Long questionId,
        @Column(name = "media_type", length = 10) String mediaType,
        @Column(name = "tmdb_id") Long tmdbId) implements Serializable {
}
