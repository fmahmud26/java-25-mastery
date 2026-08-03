package shortener.adapters;

import shortener.application.RedirectCache;
import shortener.domain.ShortUrl;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class InMemoryRedirectCache implements RedirectCache {
    private final Map<String, ShortUrl> cache = new ConcurrentHashMap<>();

    @Override
    public Optional<ShortUrl> get(String code) {
        return Optional.ofNullable(cache.get(code));
    }

    @Override
    public void put(ShortUrl url) {
        cache.put(url.code(), url);
    }

    @Override
    public void invalidate(String code) {
        cache.remove(code);
    }
}
