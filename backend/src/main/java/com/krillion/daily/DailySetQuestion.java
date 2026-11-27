package com.krillion.daily;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/** Question number "position" of a daily set. */
@Entity
@Table(name = "daily_set_question")
public class DailySetQuestion {

    @EmbeddedId
    private DailySetQuestionId id;

    @Column(name = "question_id", nullable = false)
    private Long questionId;

    protected DailySetQuestion() {
        // required by JPA
    }

    public DailySetQuestion(Long dailySetId, int position, Long questionId) {
        this.id = new DailySetQuestionId(dailySetId, (short) position);
        this.questionId = questionId;
    }

    public DailySetQuestionId getId() {
        return id;
    }

    public Long getDailySetId() {
        return id.dailySetId();
    }

    public short getPosition() {
        return id.position();
    }

    public Long getQuestionId() {
        return questionId;
    }
}
