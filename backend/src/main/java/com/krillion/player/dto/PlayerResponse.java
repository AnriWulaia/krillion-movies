package com.krillion.player.dto;

import java.util.UUID;

public record PlayerResponse(UUID id, boolean registered) {
}
