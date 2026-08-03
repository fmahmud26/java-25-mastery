package notify.application;

import notify.domain.NotificationSender;
import notify.domain.OutboxRecord;
import notify.domain.OutboxRepository;
import notify.domain.OutboxStatus;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

public final class OutboxPublisher {
    private final OutboxRepository repo;
    private final NotificationSender sender;
    private final Clock clock;
    private final int maxAttempts;
    private final Duration lease;

    public OutboxPublisher(
            OutboxRepository repo,
            NotificationSender sender,
            Clock clock,
            int maxAttempts,
            Duration lease) {
        this.repo = Objects.requireNonNull(repo);
        this.sender = Objects.requireNonNull(sender);
        this.clock = Objects.requireNonNull(clock);
        this.maxAttempts = maxAttempts;
        this.lease = Objects.requireNonNull(lease);
    }

    public int pollOnce(int batchSize) {
        Instant now = clock.instant();
        List<OutboxRecord> batch = repo.claimBatch(batchSize, now, now.plus(lease));
        int sent = 0;
        for (OutboxRecord r : batch) {
            try {
                sender.send(r);
                repo.markSent(r.id());
                sent++;
            } catch (Exception e) {
                int attempts = r.attempts() + 1;
                if (attempts >= maxAttempts) {
                    repo.markDead(r.id());
                } else {
                    Instant next = now.plusSeconds(1L << Math.min(attempts, 5));
                    repo.markRetry(r.id(), attempts, next);
                }
            }
        }
        return sent;
    }

    public long deadCount() {
        return repo.countByStatus(OutboxStatus.DEAD);
    }
}
