package events;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class InMemoryEventRepository implements EventRepository {
    private final ConcurrentHashMap<String, ProcessedEvent> store = new ConcurrentHashMap<>();

    @Override
    public void save(ProcessedEvent event) {
        store.put(event.eventId(), event);
    }

    @Override
    public Optional<ProcessedEvent> findById(String eventId) {
        return Optional.ofNullable(store.get(eventId));
    }

    @Override
    public long count() {
        return store.mappingCount();
    }

    @Override
    public List<ProcessedEvent> findAll() {
        return new ArrayList<>(store.values());
    }
}
