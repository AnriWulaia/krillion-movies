package com.krillion.attempt;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AttemptRepository extends JpaRepository<QuestionAttempt, Long> {

    Optional<QuestionAttempt> findByPlayerIdAndQuestionId(UUID playerId, Long questionId);

    //All of a player's attempts on one day, for the daily overview.
    List<QuestionAttempt> findByPlayerIdAndPlayDate(UUID playerId, LocalDate playDate);

    // Loads the attempt and locks its row until the transaction ends (SELECT ... FOR UPDATE)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from QuestionAttempt a where a.playerId = :playerId and a.questionId = :questionId")
    Optional<QuestionAttempt> findForUpdate(@Param("playerId") UUID playerId,
                                            @Param("questionId") Long questionId);

    /**
     * Starts an attempt unless one already exists (safe when two requests arrive at once).
     *
     * @return 1 if a new attempt was created, 0 if there already was one
     */
    @Modifying
    @Query(value = """
            INSERT INTO question_attempt (player_id, question_id, play_date, started_at, deadline_at)
            VALUES (:playerId, :questionId, :playDate, :startedAt, :deadlineAt)
            ON CONFLICT (player_id, question_id) DO NOTHING
            """, nativeQuery = true)
    int insertIfAbsent(@Param("playerId") UUID playerId,
                       @Param("questionId") Long questionId,
                       @Param("playDate") LocalDate playDate,
                       @Param("startedAt") Instant startedAt,
                       @Param("deadlineAt") Instant deadlineAt);
}
