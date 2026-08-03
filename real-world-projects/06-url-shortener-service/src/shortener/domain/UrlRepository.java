package shortener.domain;

import java.util.Optional;

public interface UrlRepository {
    void save(ShortUrl url);

    Optional<ShortUrl> findByCode(String code);

    boolean existsCode(String code);
}
