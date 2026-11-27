package com.krillion.scoring;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

// How many players solved a question with this exact answer. This is the "real players" part of the rarity scoring.

@Entity
@Table(name = "answer_stat")
public class AnswerStat {

    @EmbeddedId
    private AnswerStatId id;

    @Column(name = "pick_count", nullable = false)
    private int pickCount;

    protected AnswerStat() {
    }

    public AnswerStatId getId() {
        return id;
    }

    public int getPickCount() {
        return pickCount;
    }
}
