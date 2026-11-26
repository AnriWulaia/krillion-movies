package com.krillion.scoring;

import com.krillion.config.ScoringProperties;
import org.springframework.stereotype.Service;

/**
 Rarity scoring.
 share = (pickCount + prior) / (totalSolves + k)
 prior  = k * popularity / popularitySum(TMDB popularity acts as k virtual players)
 points = clamp(round(maxPoints * (1 - share)), minPoints, maxPoints)
 pickCount and totalSolves include the player who is being scored.
 */
@Service
public class ScoringService {

    private final ScoringProperties props;

    public ScoringService(ScoringProperties props) {
        this.props = props;
    }

    /**
     * How common an answer is, from 0 to 1.
     *
     * @param pickCount     solves with this exact answer
     * @param totalSolves   solves of this question with any answer
     * @param popularity    TMDB popularity of this answer
     * @param popularitySum TMDB popularity of all valid answers of the question
     */
    public double share(long pickCount, long totalSolves, double popularity, double popularitySum) {
        if (pickCount < 0 || totalSolves < 0 || pickCount > totalSolves) {
            throw new IllegalArgumentException(
                    "need 0 <= pickCount <= totalSolves, got " + pickCount + " and " + totalSolves);
        }
        if (popularity < 0 || popularitySum < 0) {
            throw new IllegalArgumentException("popularity must not be negative");
        }

        double prior = popularitySum > 0 ? props.k() * popularity / popularitySum : 0.0;
        double denominator = totalSolves + props.k();
        if (denominator == 0) {
            return 0.0;
        }
        double share = (pickCount + prior) / denominator;
        return Math.max(0.0, Math.min(1.0, share));
    }

    // Points for a correct answer: the rarer, the more.
    public int points(long pickCount, long totalSolves, double popularity, double popularitySum) {
        double share = share(pickCount, totalSolves, popularity, popularitySum);
        long raw = Math.round(props.maxPoints() * (1.0 - share));
        return (int) Math.max(props.minPoints(), Math.min(props.maxPoints(), raw));
    }

    //The same share as a percentage with one decimal, for the screen ("41.7% picked this").
    public double sharePercent(long pickCount, long totalSolves, double popularity, double popularitySum) {
        return Math.round(share(pickCount, totalSolves, popularity, popularitySum) * 1000.0) / 10.0;
    }
}
