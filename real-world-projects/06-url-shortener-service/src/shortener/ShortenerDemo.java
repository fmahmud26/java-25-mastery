package shortener;

import shortener.adapters.FixedWindowRateLimiter;
import shortener.adapters.InMemoryRedirectCache;
import shortener.adapters.InMemoryUrlRepository;
import shortener.adapters.SequenceCodeGenerator;
import shortener.application.ShortenUrlService;
import shortener.domain.ShortUrl;

import java.time.Clock;
import java.time.Duration;

/** Smoke demo for architecture wiring (extend with HttpServer). */
public final class ShortenerDemo {
    public static void main(String[] args) {
        var repo = new InMemoryUrlRepository();
        var cache = new InMemoryRedirectCache();
        var service = new ShortenUrlService(
                repo,
                new SequenceCodeGenerator(1_000_000),
                new FixedWindowRateLimiter(100, 60_000),
                Clock.systemUTC(),
                Duration.ofDays(30));

        ShortUrl created = service.shorten("demo-key", "https://example.com/a");
        System.out.println("created code=" + created.code());
        System.out.println("redirect=" + service.resolveRedirect(created.code(), cache).orElse("?"));
    }
}
