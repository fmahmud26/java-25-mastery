package shortener.adapters;

import shortener.application.RateLimiter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/** Simple fixed-window limiter for local demos (swap for Redis token bucket in prod). */
public final class FixedWindowRateLimiter implements RateLimiter {
    private final long limit;
    private final long windowMs;
    private final ConcurrentHashMap<String, Window> windows = new ConcurrentHashMap<>();

    public FixedWindowRateLimiter(long limit, long windowMs) {
        this.limit = limit;
        this.windowMs = windowMs;
    }

    @Override
    public boolean tryAcquire(String key) {
        long now = System.currentTimeMillis();
        Window w = windows.compute(key, (k, cur) -> {
            if (cur == null || now - cur.startMs >= windowMs) {
                return new Window(now, new AtomicLong(0));
            }
            return cur;
        });
        return w.count.incrementAndGet() <= limit;
    }

    private record Window(long startMs, AtomicLong count) {}
}
