package events;

import java.util.List;
import java.util.Optional;

/** Persistence port — in demos backed by ConcurrentHashMap. */
public interface EventRepository {
    void save(ProcessedEvent event);

    Optional<ProcessedEvent> findById(String eventId);

    long count();

    List<ProcessedEvent> findAll();
}
