package shortener.application;

public final class RateLimitExceededException extends RuntimeException {
    public RateLimitExceededException(String key) {
        super("rate limit exceeded for " + key);
    }
}
