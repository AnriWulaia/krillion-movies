package com.krillion.player;

import com.krillion.player.dto.PlayerResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Temporary endpoint to check that anonymous players work.
 * A missing or malformed X-Player-Id header gives 400 automatically.
 */
@RestController
@RequestMapping("/api/player")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping
    public PlayerResponse currentPlayer(@RequestHeader("X-Player-Id") UUID playerId) {
        Player player = playerService.getOrCreate(playerId);
        return new PlayerResponse(player.getId(), player.isRegistered());
    }
}
