package com.shapyfy.core.infrastructure.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@EnableCaching
public class CacheConfiguration {

    /**
     * Configure in-memory cache manager for translations.
     * Uses ConcurrentHashMap for thread-safe caching.
     * <p>
     * For production with multiple instances, consider Redis:
     * return RedisCacheManager.builder(redisConnectionFactory).build();
     */
    @Bean
    public CacheManager cacheManager() {
        log.info("Initializing ConcurrentMapCacheManager for translations");
        return new ConcurrentMapCacheManager("translations");
    }
}
