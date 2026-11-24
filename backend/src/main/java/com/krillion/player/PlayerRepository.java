package com.krillion.player;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface PlayerRepository extends JpaRepository<Player, UUID> {

    Optional<Player> findByUserId(Long userId);


    @Modifying
    @Query(value = "INSERT INTO player (id) VALUES (:id) ON CONFLICT (id) DO NOTHING",
            nativeQuery = true)
    int insertIfAbsent(@Param("id") UUID id);

    //Links an anonymous player to an account. Only works while the player has no account yet.

    @Modifying
    @Query(value = "UPDATE player SET user_id = :userId WHERE id = :id AND user_id IS NULL",
            nativeQuery = true)
    int linkToUserIfAnonymous(@Param("id") UUID id, @Param("userId") Long userId);
}
