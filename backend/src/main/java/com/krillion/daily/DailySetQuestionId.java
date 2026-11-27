package com.krillion.daily;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

/** Primary key of a daily set entry: the set and the position (1 to 7) inside it. */
@Embeddable
public record DailySetQuestionId(
        @Column(name = "daily_set_id") Long dailySetId,
        @Column(name = "position") short position) implements Serializable {
}
