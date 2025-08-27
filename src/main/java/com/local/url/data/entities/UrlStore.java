package com.local.url.data.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(name = "urls",indexes = {@Index(name = "idx_urls_expires_at",columnList = "expiresAt")})
@Data
public class UrlStore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonProperty("id")
    private Long id;

    @Column(name = "short_code", length = 12, nullable = false, unique = true)
    @JsonProperty("short_code")
    private String shortCode;

    @Column(name = "long_url", nullable = false, columnDefinition = "TEXT")
    @JsonProperty("long_url")
    private String longUrl;

    @Column(name = "created_at", nullable = false, updatable = false)
    @JsonProperty("created_at")
    private Instant createdAt = Instant.now();

    @Column(name = "expires_at", nullable = false)
    @JsonProperty("expires_at")
    private Instant expiresAt;

    @Column(name = "owner_id", length = 64)
    @JsonProperty("owner_id")
    private String ownerId;

    @Column(name = "is_active", nullable = false)
    @JsonProperty("is_active")
    private boolean isActive = true;
}