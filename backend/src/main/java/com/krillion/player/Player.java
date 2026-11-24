package com.krillion.player;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;
@Entity
@Table(name = "player")
public class Player {

    @Id
    private UUID id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Instant createdAt;

    protected Player() {
    }

    public UUID getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public boolean isRegistered() {
        return userId != null;
    }
}
