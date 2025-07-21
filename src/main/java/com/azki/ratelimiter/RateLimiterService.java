package com.azki.ratelimiter;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(value = "rate-limiter.enabled", havingValue = "true")
public class RateLimiterService {

    @Value("${rate-limiter.capacity:5}")
    private int capacity;

    @Value("${rate-limiter.refill:5}")
    private int refill;

    private final ConcurrentHashMap<RateLimiterKeyHolder, Bucket> bucketsMap = new ConcurrentHashMap<>();

    public boolean tryAcquire(String ip) {
        final RateLimiterKeyHolder keyHolder = RateLimiterKeyHolder.builder().ip(ip).build();
        final Bucket bucket = bucketsMap.computeIfAbsent(keyHolder, key -> getBucket());

        final RateLimiterKeyHolder fetchedKeyHolder = bucketsMap.keySet()
                .stream()
                .filter(k -> Objects.equals(k, keyHolder))
                .findFirst()
                .orElse(null);

        //This does not produce NullPointerException, because it exists
        fetchedKeyHolder.setCreateDateInMillis(System.currentTimeMillis());

        return bucket.tryConsume(1);
    }

    private Bucket getBucket() {
        return Bucket.builder()
                .addLimit(
                        Bandwidth.classic(
                                capacity,
                                Refill.greedy(refill, Duration.ofMinutes(1))
                        )
                )
                .build();
    }

    @Async
    @Scheduled(fixedDelay = 60_000)
    public void removeExpiredBuckets() {
        bucketsMap.entrySet().removeIf(entry -> entry.getKey().isExpired());
    }

    @Getter
    @Setter
    @Builder
    private static class RateLimiterKeyHolder {

        private String ip;

        @Builder.Default
        private long createDateInMillis = System.currentTimeMillis();

        public boolean isExpired() {
            return System.currentTimeMillis() - createDateInMillis > 60_000;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof final RateLimiterKeyHolder that)) return false;
            return Objects.equals(ip, that.ip);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(ip);
        }
    }

}
