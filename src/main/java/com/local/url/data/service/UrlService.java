package com.local.url.data.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.local.url.data.entities.UrlStore;
import com.local.url.data.repositories.UrlRepository;
import com.local.url.execution.helper.ShortCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;

import static com.local.url.util.URLShortenerLogger.logError;

@Service
public class UrlService {
    private final UrlRepository urlRepository;
    private final ShortCodeGenerator shortCodeGenerator;
    private final Cache<String,String> cache;

    @Autowired
    public UrlService(UrlRepository urlRepository, ShortCodeGenerator shortCodeGenerator, Cache<String,String> cache) {
        this.urlRepository = urlRepository;
        this.shortCodeGenerator = shortCodeGenerator;
        this.cache = cache;
    }

    @Transactional
    public UrlStore createShortUrl(String longUrl, String customCode, Instant expiresAt) {
        if (expiresAt.isBefore(Instant.now())) {
            throw new IllegalArgumentException("Expiration date must be in the future");
        }

        if(!isValid(longUrl)) {
            throw new IllegalArgumentException("Invalid URL");
        }

        // Check if the long URL already exists and is active
        return urlRepository.findByLongUrlAndIsActiveTrue(longUrl).orElseGet(() -> {
            String shortCode;
            if (customCode != null && !customCode.isEmpty()) {
                if (urlRepository.existsByShortCode(customCode)) {
                    throw new IllegalArgumentException("Custom short code already in use");
                }
                shortCode = customCode;
            } else {
                // Generate a unique short code
                do {
                    shortCode = shortCodeGenerator.generateShortCode(8);
                } while (urlRepository.existsByShortCode(shortCode));
            }

            UrlStore urlStore = new UrlStore();
            urlStore.setLongUrl(longUrl);
            urlStore.setShortCode(shortCode);
            urlStore.setExpiresAt(expiresAt);

            UrlStore savedUrl = urlRepository.save(urlStore);
            cache.put(shortCode, longUrl); // Cache the new URL mapping
            return savedUrl;
        });
    }

    private boolean isValid(String url) {
        if( url.trim().startsWith("http://") || url.trim().startsWith("https://") ) {
            try {
                new URI(url);
            } catch (URISyntaxException uriSyntaxException) {
                logError("Invalid URL passed: " + url,uriSyntaxException);
                return false;
            }
        } else {
            return false;
        }
        return true;
    }
}