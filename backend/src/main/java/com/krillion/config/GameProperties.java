package com.krillion.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "krillion.game")
public record GameProperties(
        @DefaultValue("30") int baseSeconds,          // time for each question
        @DefaultValue("2") int wrongBonusSeconds,     // extra time after a wrong guess
        @DefaultValue("5") int maxBonuses,            // how many wrong guesses give extra time
        @DefaultValue("1000") long latencyGraceMs,    // network delay we forgive after the deadline
        @DefaultValue("50") int maxGuessesPerQuestion,
        @DefaultValue("500") long minGuessIntervalMs,
        @DefaultValue("true") boolean sequential) {   // question N unlocks after N-1 is finished

    // Longest a question can last: 30 + 5 * 2 = 40 seconds with the defaults
    public int maxTotalSeconds() {
        return baseSeconds + maxBonuses * wrongBonusSeconds;
    }
}
