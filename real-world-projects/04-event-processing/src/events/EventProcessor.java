package events;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

public final class EventProcessor {
    private final EventRepository repository;
    private final AtomicLong processed = new AtomicLong();
    private final AtomicLong duplicates = new AtomicLong();

    public EventProcessor(EventRepository repository) {
        this.repository = repository;
    }

    public void process(Event event) {
        if (repository.findById(event.id()).isPresent()) {
            duplicates.incrementAndGet();
            return;
        }
        // Simulate light I/O / transform work.
        String summary = switch (event.type()) {
            case "ORDER_CREATED" -> "order=" + event.payload().getOrDefault("orderId", "?");
            case "PAYMENT" -> "amount=" + event.payload().getOrDefault("amount", "0");
            case "SHIPMENT" -> "tracking=" + event.payload().getOrDefault("tracking", "n/a");
            default -> "keys=" + event.payload().size();
        };

        repository.save(new ProcessedEvent(
                event.id(),
                event.type(),
                summary,
                Instant.now(),
                Thread.currentThread().toString()));
        processed.incrementAndGet();
    }

    public long processedCount() { return processed.get(); }
    public long duplicateCount() { return duplicates.get(); }
}
