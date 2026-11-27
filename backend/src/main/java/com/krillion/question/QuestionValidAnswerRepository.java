package com.krillion.question;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface QuestionValidAnswerRepository
        extends JpaRepository<QuestionValidAnswer, QuestionValidAnswerId> {

    // Is a guess correct?  ->  existsById(new QuestionValidAnswerId(questionId, mediaType, tmdbId))

    long countByIdQuestionId(Long questionId);

    /** Total TMDB popularity of all valid answers of a question (null if it has none). */
    @Query("select sum(a.popularity) from QuestionValidAnswer a where a.id.questionId = :questionId")
    BigDecimal sumPopularity(@Param("questionId") Long questionId);
}
