package com.krillion.attempt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GuessRepository extends JpaRepository<Guess, Long> {

    // How many distinct guesses an attempt has used (for the per-question limit).
    long countByAttemptId(Long attemptId);

    /**
     * Stores a guess unless the same one was already made in this attempt.
     *
     * @return 1 if it is a new guess, 0 if it is a duplicate
     */
    @Modifying
    @Query(value = """
            INSERT INTO guess (attempt_id, media_type, tmdb_id, is_valid)
            VALUES (:attemptId, :mediaType, :tmdbId, :valid)
            ON CONFLICT (attempt_id, media_type, tmdb_id) DO NOTHING
            """, nativeQuery = true)
    int insertIfAbsent(@Param("attemptId") Long attemptId,
                       @Param("mediaType") String mediaType,
                       @Param("tmdbId") Long tmdbId,
                       @Param("valid") boolean valid);
}
