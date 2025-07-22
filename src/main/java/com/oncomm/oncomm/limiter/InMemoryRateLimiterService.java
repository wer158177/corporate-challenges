package com.oncomm.oncomm.limiter;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InMemoryRateLimiterService {

    private final Map<String, UserAccess> rateLimitMap = new ConcurrentHashMap<>();
    private final long limit = 5; // 5회
    private final long windowSeconds = 60; // 1분

    public boolean allowRequest(String userKey) {
        UserAccess access = rateLimitMap.getOrDefault(userKey, new UserAccess());
        long now = Instant.now().getEpochSecond();

        if (now - access.windowStart >= windowSeconds) {
            access.windowStart = now;
            access.count = 1;
        } else {
            access.count += 1;
        }

        rateLimitMap.put(userKey, access);
        return access.count <= limit;
    }

    private static class UserAccess {
        long windowStart = Instant.now().getEpochSecond();
        long count = 0;
    }
}
