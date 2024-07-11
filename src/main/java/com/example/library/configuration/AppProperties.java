package com.example.library.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@ConfigurationProperties(prefix = "app.cache")
public class AppProperties {

    private final List<String> cacheNames = new ArrayList<>();

    private final Map<String, CacheProperties> caches = new HashMap<>();
    private CacheType cacheType;

    @Data
    public static class CacheProperties {
        private Duration expiry = Duration.ZERO;
    }

    public interface CacheNames {
        String BOOK_ENTITIES = "bookEntities";
        String BOOK_NAME_AUTHOR = "bookNameAuthor";
        String BOOK_BY_CATEGORY = "bookByCategory";
        String BOOK_BY_ID = "bookById";

    }

    public enum CacheType {
        REDIS
    }
}
