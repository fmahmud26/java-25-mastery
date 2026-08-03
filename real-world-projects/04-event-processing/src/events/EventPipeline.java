package events;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Wires Producer → Queue → N Consumers → Processor → Repository.
 */
public final class EventPipeline implements AutoCloseable {
    private final BlockingQueue<Event> queue;
    private final EventRepository repository;
    private final EventProcessor processor;
    private final EventProducer producer;
    private final List<EventConsumer> consumers = new ArrayList<>();
    private final ExecutorService workers;
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final List<Future<?>> consumerFutures = new ArrayList<>();

    public EventPipeline(int capacity, int consumerCount) {
        this.queue = new ArrayBlockingQueue<>(capacity);
        this.repository = new InMemoryEventRepository();
        this.processor = new EventProcessor(repository);
        this.producer = new EventProducer(queue);
        this.workers = Executors.newVirtualThreadPerTaskExecutor();

        for (int i = 0; i < consumerCount; i++) {
            EventConsumer consumer = new EventConsumer("consumer-" + i, queue, processor, running);
            consumers.add(consumer);
            consumerFutures.add(workers.submit(consumer));
        }
    }

    public void run(int eventCount) throws InterruptedException {
        long start = System.nanoTime();
        producer.produce(eventCount);
        shutdownGracefully();
        double ms = (System.nanoTime() - start) / 1_000_000.0;

        System.out.println("=== Event Pipeline Report ===");
        System.out.printf("Produced     : %d%n", producer.producedCount());
        System.out.printf("Processed    : %d%n", processor.processedCount());
        System.out.printf("Duplicates   : %d%n", processor.duplicateCount());
        System.out.printf("Persisted    : %d%n", repository.count());
        System.out.printf("Elapsed      : %.2f ms%n", ms);
        System.out.printf("Sample event : %s%n",
                repository.findById("evt-1").map(Object::toString).orElse("(missing)"));
    }

    private void shutdownGracefully() throws InterruptedException {
        running.set(false);
        queue.put(Event.POISON);
        for (Future<?> f : consumerFutures) {
            try {
                f.get(10, TimeUnit.SECONDS);
            } catch (Exception e) {
                f.cancel(true);
            }
        }
    }

    public EventRepository repository() {
        return repository;
    }

    @Override
    public void close() {
        running.set(false);
        queue.offer(Event.POISON);
        workers.shutdownNow();
    }
}
