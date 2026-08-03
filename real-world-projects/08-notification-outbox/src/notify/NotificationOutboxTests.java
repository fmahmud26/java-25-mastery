package notify;

import notify.adapters.InMemoryOutboxRepository;
import notify.adapters.LoggingSender;
import notify.application.OutboxPublisher;
import notify.application.ProductNotificationService;
import notify.domain.NotificationSender;
import notify.domain.OutboxRecord;
import notify.domain.OutboxStatus;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.concurrent.atomic.AtomicInteger;

/** Assertion suite — exit 0 = pass. */
public final class NotificationOutboxTests {
    private static int failures = 0;

    public static void main(String[] args) {
        Clock clock = Clock.fixed(Instant.parse("2026-08-03T12:00:00Z"), ZoneOffset.UTC);
        testHappyPath(clock);
        testRetryThenDead(clock);
        if (failures > 0) {
            System.err.println("FAILED: " + failures);
            System.exit(1);
        }
        System.out.println("OK NotificationOutboxTests");
    }

    private static void testHappyPath(Clock clock) {
        var repo = new InMemoryOutboxRepository();
        var product = new ProductNotificationService(repo, clock);
        String id = product.notifyUser("email", "hello", 10);
        var sender = new LoggingSender(false);
        var pub = new OutboxPublisher(repo, sender, clock, 3, Duration.ofSeconds(30));
        int sent = pub.pollOnce(10);
        expect("sent one", sent == 1);
        expect("marked sent", repo.find(id).orElseThrow().status() == OutboxStatus.SENT);
        expect("dead zero", pub.deadCount() == 0L);
    }

    private static void testRetryThenDead(Clock clock) {
        var repo = new InMemoryOutboxRepository();
        var product = new ProductNotificationService(repo, clock);
        String id = product.notifyUser("sms", "boom", 1);
        AtomicInteger calls = new AtomicInteger();
        NotificationSender failing = r -> {
            calls.incrementAndGet();
            throw new RuntimeException("provider down");
        };
        var pub = new OutboxPublisher(repo, failing, clock, 2, Duration.ofSeconds(30));
        pub.pollOnce(10);
        OutboxRecord after1 = repo.find(id).orElseThrow();
        expect("retryable after first fail", after1.status() == OutboxStatus.NEW);
        expect("attempts 1", after1.attempts() == 1);
        // availableAt is in the future — force by writing a ready record via second claim after advancing...
        // Use a mutable clock wrapper for simplicity:
        MutableClock movable = new MutableClock(clock.instant());
        var pub2 = new OutboxPublisher(repo, failing, movable, 2, Duration.ofSeconds(30));
        movable.advance(Duration.ofHours(1));
        pub2.pollOnce(10);
        expect("dead after max", repo.find(id).orElseThrow().status() == OutboxStatus.DEAD);
        expect("dead count", pub2.deadCount() == 1L);
        expect("two provider calls", calls.get() == 2);
    }

    private static void expect(String name, boolean ok) {
        if (!ok) {
            failures++;
            System.err.println("ASSERT " + name);
        }
    }

    private static final class MutableClock extends Clock {
        private Instant instant;
        MutableClock(Instant start) { this.instant = start; }
        void advance(Duration d) { instant = instant.plus(d); }
        @Override public ZoneOffset getZone() { return ZoneOffset.UTC; }
        @Override public Clock withZone(java.time.ZoneId zone) { return this; }
        @Override public Instant instant() { return instant; }
    }
}
