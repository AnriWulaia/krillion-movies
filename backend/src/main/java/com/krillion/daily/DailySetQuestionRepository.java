package com.krillion.daily;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DailySetQuestionRepository
        extends JpaRepository<DailySetQuestion, DailySetQuestionId> {

    /** The questions of a daily set in order, position 1 to 7. */
    List<DailySetQuestion> findByIdDailySetIdOrderByIdPositionAsc(Long dailySetId);

    /** Is this question part of that set? (also gives its position) */
    Optional<DailySetQuestion> findByIdDailySetIdAndQuestionId(Long dailySetId, Long questionId);
}
