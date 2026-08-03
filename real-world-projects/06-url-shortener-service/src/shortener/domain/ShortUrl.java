package shortener.domain;

import java.time.Instant;
import java.util.Objects;

/** Immutable short-url aggregate root facts. */
public record ShortUrl(String code, String longUrl, Instant createdAt, Instant expiresAt) {
    public ShortUrl {
        Objects.requireNonNull(code);
        Objects.requireNonNull(longUrl);
        Objects.requireNonNull(createdAt);
        if (code.isBlank()) throw new IllegalArgumentException("code");
        if (!(longUrl.startsWith("https://") || longUrl.startsWith("http://"))) {
            throw new IllegalArgumentException("only http(s) urls");
        }
    }

    public boolean isExpired(Instant now) {
        return expiresAt != null && !now.isBefore(expiresAt);
    }
}
