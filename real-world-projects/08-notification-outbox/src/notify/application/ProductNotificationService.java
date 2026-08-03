package notify.application;

import notify.domain.OutboxRecord;
import notify.domain.OutboxRepository;
import notify.domain.OutboxStatus;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/** Simulates product service writing domain + outbox in one transaction. */
public final class ProductNotificationService {
    private final OutboxRepository outbox;
    private final Clock clock;
    private final Object txn = new Object();

    public ProductNotificationService(OutboxRepository outbox, Clock clock) {
        this.outbox = Objects.requireNonNull(outbox);
        this.clock = Objects.requireNonNull(clock);
    }

    public String notifyUser(String channel, String payload, int priority) {
        synchronized (txn) {
            // domain write would happen here in the same DB transaction
            Instant now = clock.instant();
            String id = UUID.randomUUID().toString();
            outbox.append(new OutboxRecord(id, channel, payload, priority, OutboxStatus.NEW, 0, now, null));
            return id;
        }
    }
}
