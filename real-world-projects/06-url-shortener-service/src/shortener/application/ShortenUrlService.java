package shortener.application;

import shortener.domain.CodeGenerator;
import shortener.domain.ShortUrl;
import shortener.domain.UrlRepository;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

public final class ShortenUrlService {
    private final UrlRepository repo;
    private final CodeGenerator codes;
    private final RateLimiter limiter;
    private final Clock clock;
    private final Duration defaultTtl;

    public ShortenUrlService(
            UrlRepository repo,
            CodeGenerator codes,
            RateLimiter limiter,
            Clock clock,
            Duration defaultTtl) {
        this.repo = Objects.requireNonNull(repo);
        this.codes = Objects.requireNonNull(codes);
        this.limiter = Objects.requireNonNull(limiter);
        this.clock = Objects.requireNonNull(clock);
        this.defaultTtl = Objects.requireNonNull(defaultTtl);
    }

    public ShortUrl shorten(String apiKey, String longUrl) {
        if (!limiter.tryAcquire(apiKey)) {
            throw new RateLimitExceededException(apiKey);
        }
        Instant now = clock.instant();
        for (int attempt = 0; attempt < 5; attempt++) {
            String code = codes.nextCode();
            if (repo.existsCode(code)) continue;
            var entity = new ShortUrl(code, longUrl, now, now.plus(defaultTtl));
            repo.save(entity);
            return entity;
        }
        throw new IllegalStateException("code allocation exhausted retries");
    }

    public Optional<String> resolveRedirect(String code, RedirectCache cache) {
        Optional<ShortUrl> cached = cache.get(code);
        ShortUrl url = cached.orElseGet(() -> repo.findByCode(code).orElse(null));
        if (url == null) return Optional.empty();
        if (cached.isEmpty()) cache.put(url);
        if (url.isExpired(clock.instant())) return Optional.empty();
        return Optional.of(url.longUrl());
    }
}
