package com.local.url.execution.controllers;

import com.local.url.data.entities.UrlStore;
import com.local.url.data.service.UrlService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;

@RestController
public class UrlController {

    private final UrlService urlService;

    @Autowired
    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/api/shorten")
    public Map<String,String> shortenUrl(@RequestBody Map<String,String> request, HttpServletRequest httpServletRequest) {
        String originalUrl = request.get("long_url");
        String customCode = request.get("custom_code");
        Instant expiresAt = request.containsKey("expires_at") ? Instant.parse(request.get("expires_at")) : null;

        UrlStore shortUrl = urlService.createShortUrl(originalUrl, customCode, expiresAt);

        return Map.of("short_url", httpServletRequest.getScheme() + "://" + httpServletRequest.getServerName() + (httpServletRequest.getServerPort() == 80 || httpServletRequest.getServerPort() == 443 ? "" : ":" + httpServletRequest.getServerPort()) + "/" + shortUrl.getShortCode());
    }

    @GetMapping("/{code}")
    public ResponseEntity<Void> redirect(@PathVariable String code) {
        Optional<String> longUrl = urlService.resolveShortCode(code);
        return longUrl.<ResponseEntity<Void>>map(s -> ResponseEntity.status(HttpStatus.FOUND).location(URI.create(s)).build()).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
