package shortener.application;

import shortener.domain.ShortUrl;
import shortener.domain.UrlRepository;

import java.util.Optional;

public interface RedirectCache {
    Optional<ShortUrl> get(String code);

    void put(ShortUrl url);

    void invalidate(String code);
}
