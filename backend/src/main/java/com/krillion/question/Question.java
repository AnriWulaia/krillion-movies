package com.krillion.question;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.ColumnTransformer;

import java.time.Instant;

/** A question and the rule behind it. The valid answers live in QuestionValidAnswer. */
@Entity
@Table(name = "question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "text")
    private String text;

    @Enumerated(EnumType.STRING)
    @Column(name = "rule_type", nullable = false, length = 40)
    private RuleType ruleType;

    // JSON text kept in a jsonb column. cast(? as jsonb) lets PostgreSQL accept our String on insert.
    @Column(name = "rule_params", nullable = false, columnDefinition = "jsonb")
    @ColumnTransformer(write = "cast(? as jsonb)")
    private String ruleParams;

    /** What the search box should offer: "movie", "tv" or "person" (see TmdbType). */
    @Column(name = "accepted_type", nullable = false, length = 10)
    private String acceptedType;

    @Column(name = "anchor_key", nullable = false, length = 80)
    private String anchorKey;

    @Column(name = "question_hash", nullable = false, length = 64)
    private String questionHash;

    @Column(name = "valid_answer_count", nullable = false)
    private int validAnswerCount;

    // filled by the database default (now())
    @Column(name = "created_at", insertable = false, updatable = false)
    private Instant createdAt;

    protected Question() {
        // required by JPA
    }

    public Question(String text, RuleType ruleType, String ruleParams, String acceptedType,
                    String anchorKey, String questionHash, int validAnswerCount) {
        this.text = text;
        this.ruleType = ruleType;
        this.ruleParams = ruleParams;
        this.acceptedType = acceptedType;
        this.anchorKey = anchorKey;
        this.questionHash = questionHash;
        this.validAnswerCount = validAnswerCount;
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public RuleType getRuleType() {
        return ruleType;
    }

    public String getRuleParams() {
        return ruleParams;
    }

    public String getAcceptedType() {
        return acceptedType;
    }

    public String getAnchorKey() {
        return anchorKey;
    }

    public String getQuestionHash() {
        return questionHash;
    }

    public int getValidAnswerCount() {
        return validAnswerCount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
