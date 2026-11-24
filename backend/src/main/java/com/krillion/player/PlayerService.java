package com.krillion.player;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }


    @Transactional
    public Player getOrCreate(UUID playerId) {
        return playerRepository.findById(playerId).orElseGet(() -> {
            playerRepository.insertIfAbsent(playerId);
            return playerRepository.findById(playerId).orElseThrow();
        });
    }

    @Transactional(readOnly = true)
    public Player getByUserId(Long userId) {
        return playerRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException("User " + userId + " has no player"));
    }

    @Transactional
    public UUID attachToUser(UUID anonymousPlayerId, Long userId) {
        if (anonymousPlayerId != null) {
            playerRepository.insertIfAbsent(anonymousPlayerId);
            if (playerRepository.linkToUserIfAnonymous(anonymousPlayerId, userId) == 1) {
                return anonymousPlayerId;
            }
        }
        UUID freshId = UUID.randomUUID();
        playerRepository.insertIfAbsent(freshId);
        playerRepository.linkToUserIfAnonymous(freshId, userId);
        return freshId;
    }
}
