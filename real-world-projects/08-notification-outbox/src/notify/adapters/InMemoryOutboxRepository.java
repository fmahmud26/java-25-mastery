package notify.adapters;

import notify.domain.OutboxRecord;
import notify.domain.OutboxRepository;
import notify.domain.OutboxStatus;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class InMemoryOutboxRepository implements OutboxRepository {
    private final ConcurrentHashMap<String, OutboxRecord> store = new ConcurrentHashMap<>();
    private final Object lock = new Object();

    @Override
    public void append(OutboxRecord record) {
        if (store.putIfAbsent(record.id(), record) != null) {
            throw new IllegalStateException("duplicate outbox id");
        }
    }

    @Override
    public List<OutboxRecord> claimBatch(int limit, Instant now, Instant leaseUntil) {
        synchronized (lock) {
            List<OutboxRecord> candidates = store.values().stream()
                    .filter(r -> r.status() == OutboxStatus.NEW
                            || (r.status() == OutboxStatus.IN_FLIGHT
                            && r.leasedUntil() != null
                            && r.leasedUntil().isBefore(now)))
                    .filter(r -> !r.availableAt().isAfter(now))
                    .sorted(Comparator
                            .comparingInt(OutboxRecord::priority).reversed()
                            .thenComparing(OutboxRecord::availableAt))
                    .limit(limit)
                    .toList();
            List<OutboxRecord> claimed = new ArrayList<>();
            for (OutboxRecord r : candidates) {
                OutboxRecord next = r.withStatus(OutboxStatus.IN_FLIGHT, r.attempts(), r.availableAt(), leaseUntil);
                store.put(r.id(), next);
                claimed.add(next);
            }
            return claimed;
        }
    }

    @Override
    public void markSent(String id) {
        update(id, r -> r.withStatus(OutboxStatus.SENT, r.attempts(), r.availableAt(), null));
    }

    @Override
    public void markRetry(String id, int attempts, Instant availableAt) {
        update(id, r -> r.withStatus(OutboxStatus.NEW, attempts, availableAt, null));
    }

    @Override
    public void markDead(String id) {
        update(id, r -> r.withStatus(OutboxStatus.DEAD, r.attempts(), r.availableAt(), null));
    }

    @Override
    public Optional<OutboxRecord> find(String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public long countByStatus(OutboxStatus status) {
        return store.values().stream().filter(r -> r.status() == status).count();
    }

    private void update(String id, java.util.function.Function<OutboxRecord, OutboxRecord> fn) {
        synchronized (lock) {
            OutboxRecord cur = store.get(id);
            if (cur == null) throw new IllegalArgumentException(id);
            store.put(id, fn.apply(cur));
        }
    }
}
