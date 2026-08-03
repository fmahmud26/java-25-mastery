package shortener;

import shortener.adapters.FixedWindowRateLimiter;
import shortener.adapters.InMemoryRedirectCache;
import shortener.adapters.InMemoryUrlRepository;
import shortener.adapters.SequenceCodeGenerator;
import shortener.application.RateLimitExceededException;
import shortener.application.ShortenUrlService;
import shortener.domain.ShortUrl;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;

/** Assertion suite — exit 0 = pass. */
public final class ShortenerTests {
    private static int failures = 0;

    public static void main(String[] args) {
        Clock clock = Clock.fixed(Instant.parse("2026-08-03T12:00:00Z"), ZoneOffset.UTC);
        testShortenAndResolve(clock);
        testRateLimit(clock);
        testExpired(clock);
        if (failures > 0) {
            System.err.println("FAILED: " + failures);
            System.exit(1);
        }
        System.out.println("OK ShortenerTests");
    }

    private static void testShortenAndResolve(Clock clock) {
        var repo = new InMemoryUrlRepository();
        var svc = new ShortenUrlService(
                repo, new SequenceCodeGenerator(1), new FixedWindowRateLimiter(100, 60_000), clock, Duration.ofDays(1));
        ShortUrl u = svc.shorten("k1", "https://example.com/a");
        var cache = new InMemoryRedirectCache();
        expect("resolve", svc.resolveRedirect(u.code(), cache).orElseThrow().equals("https://example.com/a"));
        expect("cache fill", cache.get(u.code()).isPresent());
    }

    private static void testRateLimit(Clock clock) {
        var repo = new InMemoryUrlRepository();
        var svc = new ShortenUrlService(
                repo, new SequenceCodeGenerator(1), new FixedWindowRateLimiter(2, 60_000), clock, Duration.ofDays(1));
        svc.shorten("limited", "https://example.com/1");
        svc.shorten("limited", "https://example.com/2");
        boolean threw = false;
        try {
            svc.shorten("limited", "https://example.com/3");
        } catch (RateLimitExceededException e) {
            threw = true;
        }
        expect("rate limited", threw);
    }

    private static void testExpired(Clock clock) {
        var repo = new InMemoryUrlRepository();
        var svc = new ShortenUrlService(
                repo, new SequenceCodeGenerator(1), new FixedWindowRateLimiter(10, 60_000), clock, Duration.ofSeconds(0));
        // TTL 0 → expires immediately at same instant depending on isExpired semantics
        ShortUrl u = svc.shorten("k2", "https://example.com/x");
        // Advance via new service with later clock
        Clock later = Clock.fixed(clock.instant().plusSeconds(1), ZoneOffset.UTC);
        var svc2 = new ShortenUrlService(
                repo, new SequenceCodeGenerator(1), new FixedWindowRateLimiter(10, 60_000), later, Duration.ofDays(1));
        expect("expired empty", svc2.resolveRedirect(u.code(), new InMemoryRedirectCache()).isEmpty());
    }

    private static void expect(String name, boolean ok) {
        if (!ok) {
            failures++;
            System.err.println("ASSERT " + name);
        }
    }
}
