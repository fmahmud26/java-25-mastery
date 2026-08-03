package shortener.application;

public interface RateLimiter {
    /** @return true if the call is allowed */
    boolean tryAcquire(String key);
}
