package shortener.adapters;

import shortener.domain.ShortUrl;
import shortener.domain.UrlRepository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class InMemoryUrlRepository implements UrlRepository {
    private final Map<String, ShortUrl> store = new ConcurrentHashMap<>();

    @Override
    public void save(ShortUrl url) {
        if (store.putIfAbsent(url.code(), url) != null) {
            throw new IllegalStateException("duplicate code");
        }
    }

    @Override
    public Optional<ShortUrl> findByCode(String code) {
        return Optional.ofNullable(store.get(code));
    }

    @Override
    public boolean existsCode(String code) {
        return store.containsKey(code);
    }
}
