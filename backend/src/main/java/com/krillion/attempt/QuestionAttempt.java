package com.krillion.attempt;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/**
 * One player's go at one question. Holds the server-side timer (deadlineAt)
 * and the result. There is at most one per player and question.
 */
@Entity
@Table(name = "question_attempt")
public class QuestionAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "player_id", nullable = false)
    private UUID playerId;

    @Column(name = "question_id", nullable = false)
    private Long questionId;

    @Column(name = "play_date", nullable = false)
    private LocalDate playDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 12)
    private AttemptStatus status;

    @Column(name = "started_at", nullable = false)
    private Instant startedAt;

    @Column(name = "deadline_at", nullable = false)
    private Instant deadlineAt;

    @Column(name = "bonuses_used", nullable = false)
    private short bonusesUsed;

    @Column(name = "wrong_guess_count", nullable = false)
    private int wrongGuessCount;

    @Column(name = "solved_media_type", length = 10)
    private String solvedMediaType;

    @Column(name = "solved_tmdb_id")
    private Long solvedTmdbId;

    @Column(name = "points_provisional", nullable = false)
    private int pointsProvisional;

    @Column(name = "points_final")
    private Integer pointsFinal;

    @Column(name = "finished_at")
    private Instant finishedAt;

    protected QuestionAttempt() {
    }

    public static QuestionAttempt start(UUID playerId, Long questionId, LocalDate playDate,
                                        Instant now, Instant deadline) {
        QuestionAttempt attempt = new QuestionAttempt();
        attempt.playerId = playerId;
        attempt.questionId = questionId;
        attempt.playDate = playDate;
        attempt.status = AttemptStatus.IN_PROGRESS;
        attempt.startedAt = now;
        attempt.deadlineAt = deadline;
        return attempt;
    }

    /** A wrong guess: counts it and moves the deadline (AttemptTimer.afterWrongGuess). */
    public void recordWrongGuess(Instant newDeadline, int newBonusesUsed) {
        this.wrongGuessCount++;
        this.deadlineAt = newDeadline;
        this.bonusesUsed = (short) newBonusesUsed;
    }

    public void markSolved(String mediaType, long tmdbId, int points, Instant now) {
        this.status = AttemptStatus.SOLVED;
        this.solvedMediaType = mediaType;
        this.solvedTmdbId = tmdbId;
        this.pointsProvisional = points;
        this.finishedAt = now;
    }

    public void markExpired(Instant now) {
        this.status = AttemptStatus.EXPIRED;
        this.finishedAt = now;
    }

    public void setPointsFinal(int pointsFinal) {
        this.pointsFinal = pointsFinal;
    }

    public boolean isInProgress() {
        return status == AttemptStatus.IN_PROGRESS;
    }

    public Long getId() {
        return id;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public LocalDate getPlayDate() {
        return playDate;
    }

    public AttemptStatus getStatus() {
        return status;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public Instant getDeadlineAt() {
        return deadlineAt;
    }

    public int getBonusesUsed() {
        return bonusesUsed;
    }

    public int getWrongGuessCount() {
        return wrongGuessCount;
    }

    public String getSolvedMediaType() {
        return solvedMediaType;
    }

    public Long getSolvedTmdbId() {
        return solvedTmdbId;
    }

    public int getPointsProvisional() {
        return pointsProvisional;
    }

    public Integer getPointsFinal() {
        return pointsFinal;
    }

    /** Points to show: the final ones once the day is finalized, otherwise the provisional ones. */
    public int getPoints() {
        return pointsFinal != null ? pointsFinal : pointsProvisional;
    }

    public Instant getFinishedAt() {
        return finishedAt;
    }
}
