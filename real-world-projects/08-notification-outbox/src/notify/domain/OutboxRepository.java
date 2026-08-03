package notify.domain;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface OutboxRepository {
    void append(OutboxRecord record);

    List<OutboxRecord> claimBatch(int limit, Instant now, Instant leaseUntil);

    void markSent(String id);

    void markRetry(String id, int attempts, Instant availableAt);

    void markDead(String id);

    Optional<OutboxRecord> find(String id);

    long countByStatus(OutboxStatus status);
}
