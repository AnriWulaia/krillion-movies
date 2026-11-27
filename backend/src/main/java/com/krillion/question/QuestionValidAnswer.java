package com.krillion.question;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.math.BigDecimal;

/** One correct answer to a question, taken from TMDB when the question was created. */
@Entity
@Table(name = "question_valid_answer")
public class QuestionValidAnswer {

    @EmbeddedId
    private QuestionValidAnswerId id;

    @Column(nullable = false, length = 300)
    private String title;

    private Integer year;

    /** TMDB popularity, used as the starting guess for how common this answer is. */
    @Column(nullable = false, precision = 10, scale = 3)
    private BigDecimal popularity;

    protected QuestionValidAnswer() {
        // required by JPA
    }

    public QuestionValidAnswer(Long questionId, String mediaType, Long tmdbId,
                               String title, Integer year, BigDecimal popularity) {
        this.id = new QuestionValidAnswerId(questionId, mediaType, tmdbId);
        this.title = title;
        this.year = year;
        this.popularity = popularity;
    }

    public QuestionValidAnswerId getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Integer getYear() {
        return year;
    }

    public BigDecimal getPopularity() {
        return popularity;
    }
}
