package com.krillion.attempt;

import com.krillion.config.GameProperties;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class AttemptTimerTest {

    private static final Instant START = Instant.parse("2026-11-25T10:00:00Z");

    // 30 s base, +2 s per wrong guess, at most 5 bonuses, 1 s grace
    private final AttemptTimer timer =
            new AttemptTimer(new GameProperties(30, 2, 5, 1000, 50, 500, true));

    @Test
    void questionStartsWithThirtySeconds() {
        assertThat(timer.initialDeadline(START)).isEqualTo(START.plusSeconds(30));
    }

    @Test
    void wrongGuessAddsTwoSecondsAndUsesUpOneBonus() {
        Instant deadline = timer.initialDeadline(START);

        AttemptTimer.Extension result = timer.afterWrongGuess(deadline, 0);

        assertThat(result.deadline()).isEqualTo(deadline.plusSeconds(2));
        assertThat(result.bonusesUsed()).isEqualTo(1);
    }

    @Test
    void onlyFiveWrongGuessesGiveExtraTime() {
        Instant deadline = timer.initialDeadline(START);
        int bonuses = 0;

        for (int wrongGuess = 1; wrongGuess <= 10; wrongGuess++) {
            AttemptTimer.Extension result = timer.afterWrongGuess(deadline, bonuses);
            deadline = result.deadline();
            bonuses = result.bonusesUsed();
        }

        assertThat(bonuses).isEqualTo(5);
        assertThat(deadline).isEqualTo(START.plusSeconds(40)); // 30 + 5 * 2
    }

    @Test
    void noExtraTimeOnceTheLimitIsReached() {
        Instant deadline = timer.initialDeadline(START);

        AttemptTimer.Extension result = timer.afterWrongGuess(deadline, 5);

        assertThat(result.deadline()).isEqualTo(deadline);
        assertThat(result.bonusesUsed()).isEqualTo(5);
    }

    @Test
    void guessIsAcceptedUntilTheDeadlinePlusGrace() {
        Instant deadline = START.plusSeconds(30);

        assertThat(timer.isExpired(deadline, deadline.minusSeconds(1))).isFalse();
        assertThat(timer.isExpired(deadline, deadline)).isFalse();
        assertThat(timer.isExpired(deadline, deadline.plusMillis(1000))).isFalse();
        assertThat(timer.isExpired(deadline, deadline.plusMillis(1001))).isTrue();
    }

    @Test
    void maxTotalTimeIsFortySecondsWithDefaults() {
        assertThat(new GameProperties(30, 2, 5, 1000, 50, 500, true).maxTotalSeconds()).isEqualTo(40);
    }

    @Test
    void usesTheConfiguredValues() {
        // 10 s base, +3 s per wrong guess, only 2 bonuses, no grace
        AttemptTimer custom = new AttemptTimer(new GameProperties(10, 3, 2, 0, 50, 500, true));
        Instant deadline = custom.initialDeadline(START);
        assertThat(deadline).isEqualTo(START.plusSeconds(10));

        AttemptTimer.Extension first = custom.afterWrongGuess(deadline, 0);
        AttemptTimer.Extension second = custom.afterWrongGuess(first.deadline(), first.bonusesUsed());
        AttemptTimer.Extension third = custom.afterWrongGuess(second.deadline(), second.bonusesUsed());

        assertThat(third.deadline()).isEqualTo(START.plusSeconds(16));
        assertThat(custom.isExpired(third.deadline(), third.deadline().plusMillis(1))).isTrue();
    }
}
