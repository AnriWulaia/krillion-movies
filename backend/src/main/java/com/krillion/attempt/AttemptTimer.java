package com.krillion.attempt;

import com.krillion.config.GameProperties;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 a question starts with baseSeconds (30)
 each wrong guess adds wrongBonusSeconds (2), for at most maxBonuses (5) wrong guesses
 a guess is still accepted for latencyGraceMs (1 s) after the deadline
 */
@Component
public class AttemptTimer {

    private final GameProperties props;

    public AttemptTimer(GameProperties props) {
        this.props = props;
    }

    public Instant initialDeadline(Instant startedAt) {
        return startedAt.plusSeconds(props.baseSeconds());
    }

    //True when it is too late to accept a guess
    public boolean isExpired(Instant deadline, Instant now) {
        return now.isAfter(deadline.plusMillis(props.latencyGraceMs()));
    }

    //The new deadline and bonus count after a wrong guess. Unchanged once the bonus limit is used up.
    public Extension afterWrongGuess(Instant deadline, int bonusesUsed) {
        if (bonusesUsed >= props.maxBonuses()) {
            return new Extension(deadline, bonusesUsed);
        }
        return new Extension(deadline.plusSeconds(props.wrongBonusSeconds()), bonusesUsed + 1);
    }

    public record Extension(Instant deadline, int bonusesUsed) {
    }
}
