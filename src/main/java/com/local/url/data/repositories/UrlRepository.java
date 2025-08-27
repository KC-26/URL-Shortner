package com.local.url.data.repositories;

import com.local.url.data.entities.UrlStore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UrlRepository extends JpaRepository<UrlStore,Long> {
    Optional<UrlStore> findByShortCode(String shortCode);
    boolean existsByShortCode(String shortCode);
    Optional<UrlStore> findByLongUrlAndIsActiveTrue(String longUrl);
}
