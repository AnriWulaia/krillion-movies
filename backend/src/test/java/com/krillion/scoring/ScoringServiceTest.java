package com.krillion.scoring;

import com.krillion.config.ScoringProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

class ScoringServiceTest {

    // k = 20 virtual players, points between 10 and 100
    private final ScoringService scoring = new ScoringService(new ScoringProperties(20, 10, 100));

    @ParameterizedTest(name = "{5}")
    @CsvSource({
            // pickCount, totalSolves, popularity, popularitySum, expectedPoints, description
            "0,    0,    50, 100, 50,  famous answer on day one is already common",
            "0,    0,    1,  100, 99,  obscure answer on day one is rare",
            "0,    0,    0,  0,   100, no data at all gives full points",
            "1,    10,   10, 100, 90,  one solve out of ten",
            "50,   100,  0,  100, 58,  half of the solves without a popularity prior",
            "9,    10,   0,  0,   70,  popularity unknown so only the real counts matter",
            "100,  100,  100, 100, 10, everybody picked it so the floor applies",
            "1000, 1000, 0,  100, 10,  floor also applies with many players"
    })
    void calculatesPoints(long pick, long total, double popularity, double popularitySum,
                          int expected, String description) {
        assertThat(scoring.points(pick, total, popularity, popularitySum)).isEqualTo(expected);
    }

    @Test
    void shareIsAFractionBetweenZeroAndOne() {
        assertThat(scoring.share(50, 100, 0, 100)).isCloseTo(0.41667, within(0.0001));
        assertThat(scoring.share(0, 0, 50, 100)).isEqualTo(0.5);
    }

    @Test
    void sharePercentIsRoundedToOneDecimal() {
        assertThat(scoring.sharePercent(50, 100, 0, 100)).isEqualTo(41.7);
    }

    @Test
    void pointsAlwaysStayBetweenMinAndMax() {
        for (long total = 0; total <= 50; total++) {
            for (long pick = 0; pick <= total; pick++) {
                for (double popularity = 0; popularity <= 100; popularity += 25) {
                    int points = scoring.points(pick, total, popularity, 100);
                    assertThat(points).isBetween(10, 100);
                }
            }
        }
    }

    @Test
    void rarerAnswersNeverScoreLessThanCommonOnes() {
        int rare = scoring.points(1, 100, 0, 100);
        int common = scoring.points(60, 100, 0, 100);
        assertThat(rare).isGreaterThan(common);
    }

    @Test
    void rejectsMorePicksThanSolves() {
        assertThatThrownBy(() -> scoring.points(5, 3, 0, 0))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejectsNegativeNumbers() {
        assertThatThrownBy(() -> scoring.points(-1, 3, 0, 0))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> scoring.points(1, 3, -5, 100))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
