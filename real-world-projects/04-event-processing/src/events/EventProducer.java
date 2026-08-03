package events;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

public final class EventProducer {
    private final BlockingQueue<Event> queue;
    private final AtomicLong produced = new AtomicLong();

    public EventProducer(BlockingQueue<Event> queue) {
        this.queue = queue;
    }

    public void produce(int count) throws InterruptedException {
        String[] types = {"ORDER_CREATED", "PAYMENT", "SHIPMENT", "AUDIT"};
        for (int i = 1; i <= count; i++) {
            String type = types[i % types.length];
            Event event = new Event(
                    "evt-" + i,
                    type,
                    Map.of(
                            "orderId", "ORD-" + (i % 50),
                            "amount", String.valueOf(10 + (i % 90)),
                            "tracking", "TRK-" + i),
                    Instant.now());
            queue.put(event);
            produced.incrementAndGet();
        }
    }

    public long producedCount() {
        return produced.get();
    }
}
