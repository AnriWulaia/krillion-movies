package com.krillion.daily;

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

/** The set of 7 questions for one calendar day. */
@Entity
@Table(name = "daily_set")
public class DailySet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "play_date", nullable = false)
    private LocalDate playDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DailySetStatus status = DailySetStatus.ACTIVE;

    // filled by the database default (now())
    @Column(name = "created_at", insertable = false, updatable = false)
    private Instant createdAt;

    protected DailySet() {
        // required by JPA
    }

    public DailySet(LocalDate playDate) {
        this.playDate = playDate;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getPlayDate() {
        return playDate;
    }

    public DailySetStatus getStatus() {
        return status;
    }

    public void markFinalized() {
        this.status = DailySetStatus.FINALIZED;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
