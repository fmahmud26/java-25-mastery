package notify.domain;

import java.time.Instant;
import java.util.Objects;

public record OutboxRecord(
        String id,
        String channel,
        String payload,
        int priority,
        OutboxStatus status,
        int attempts,
        Instant availableAt,
        Instant leasedUntil) {

    public OutboxRecord {
        Objects.requireNonNull(id);
        Objects.requireNonNull(channel);
        Objects.requireNonNull(payload);
        Objects.requireNonNull(status);
        Objects.requireNonNull(availableAt);
    }

    public OutboxRecord withStatus(OutboxStatus s, int attempts, Instant availableAt, Instant leasedUntil) {
        return new OutboxRecord(id, channel, payload, priority, s, attempts, availableAt, leasedUntil);
    }
}
