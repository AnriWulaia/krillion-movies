package com.krillion.auth.dto;

import java.util.UUID;

// What the frontend gets after register, login and GET /api/me
public record UserResponse(String email, String displayName, UUID playerId) {
}
