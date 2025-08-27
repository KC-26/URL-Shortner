package com.local.url.execution.controllers;

import com.local.url.data.entities.UrlStore;
import com.local.url.data.service.UrlService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

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
}
