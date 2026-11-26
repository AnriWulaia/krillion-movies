package com.krillion.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "krillion.scoring")
public record ScoringProperties(
        @DefaultValue("20") double k,          // virtual starter players for the popularity prior
        @DefaultValue("10") int minPoints,     // a correct answer is always worth at least this
        @DefaultValue("100") int maxPoints) {  // points for the rarest possible answer

    public ScoringProperties {
        if (k < 0) {
            throw new IllegalArgumentException("k must not be negative");
        }
        if (minPoints < 0 || minPoints > maxPoints) {
            throw new IllegalArgumentException("need 0 <= minPoints <= maxPoints");
        }
    }
}
