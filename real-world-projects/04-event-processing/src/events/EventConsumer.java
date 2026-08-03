package events;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public final class EventConsumer implements Runnable {
    private final String name;
    private final BlockingQueue<Event> queue;
    private final EventProcessor processor;
    private final AtomicBoolean running;
    private final AtomicLong consumed = new AtomicLong();

    public EventConsumer(String name,
                         BlockingQueue<Event> queue,
                         EventProcessor processor,
                         AtomicBoolean running) {
        this.name = name;
        this.queue = queue;
        this.processor = processor;
        this.running = running;
    }

    @Override
    public void run() {
        try {
            while (running.get() || !queue.isEmpty()) {
                Event event = queue.take();
                if (event.isPoison()) {
                    // Re-offer so sibling consumers can exit too.
                    queue.offer(Event.POISON);
                    break;
                }
                processor.process(event);
                consumed.incrementAndGet();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.err.printf("%s stopped after %d events%n", name, consumed.get());
    }

    public long consumedCount() {
        return consumed.get();
    }
}
