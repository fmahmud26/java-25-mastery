package perf;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Runs the same blocking workload under three concurrency models.
 */
public final class WorkloadBench {
    private final int tasks;
    private final long delayMs;
    private final int platformThreads;
    private final RuntimeSnapshot snapshot = new RuntimeSnapshot();

    public WorkloadBench(int tasks, long delayMs, int platformThreads) {
        this.tasks = tasks;
        this.delayMs = delayMs;
        this.platformThreads = platformThreads;
    }

    public BenchResult platformThreads() throws InterruptedException {
        ThreadFactory factory = Thread.ofPlatform().name("plat-", 0).factory();
        try (ExecutorService pool = Executors.newFixedThreadPool(platformThreads, factory)) {
            return runOnExecutor("Platform Threads (pool=" + platformThreads + ")", pool);
        }
    }

    public BenchResult virtualThreads() throws InterruptedException {
        try (ExecutorService pool = Executors.newVirtualThreadPerTaskExecutor()) {
            return runOnExecutor("Virtual Threads", pool);
        }
    }

    public BenchResult completableFutures() throws InterruptedException {
        try (ExecutorService pool = Executors.newVirtualThreadPerTaskExecutor()) {
            return runCompletable("CompletableFuture + VT executor", pool);
        }
    }

    private BenchResult runOnExecutor(String name, ExecutorService pool) throws InterruptedException {
        warmup(pool, Math.min(50, tasks));
        snapshot.hintGc();

        long[] samples = new long[tasks];
        AtomicInteger peakThreads = new AtomicInteger(snapshot.threadCount());
        CountDownLatch done = new CountDownLatch(tasks);

        long start = System.nanoTime();
        for (int i = 0; i < tasks; i++) {
            final int idx = i;
            pool.execute(() -> {
                long t0 = System.nanoTime();
                block();
                samples[idx] = System.nanoTime() - t0;
                peakThreads.updateAndGet(p -> Math.max(p, snapshot.threadCount()));
                done.countDown();
            });
        }
        done.await();
        long wall = System.nanoTime() - start;
        return new BenchResult(name, tasks, wall, samples, peakThreads.get(), snapshot.usedMemoryBytes());
    }

    private BenchResult runCompletable(String name, ExecutorService pool) throws InterruptedException {
        warmup(pool, Math.min(50, tasks));
        snapshot.hintGc();

        long[] samples = new long[tasks];
        AtomicInteger peakThreads = new AtomicInteger(snapshot.threadCount());

        long start = System.nanoTime();
        @SuppressWarnings("unchecked")
        CompletableFuture<Void>[] futures = new CompletableFuture[tasks];
        for (int i = 0; i < tasks; i++) {
            final int idx = i;
            futures[idx] = CompletableFuture.runAsync(() -> {
                long t0 = System.nanoTime();
                block();
                samples[idx] = System.nanoTime() - t0;
                peakThreads.updateAndGet(p -> Math.max(p, snapshot.threadCount()));
            }, pool);
        }
        CompletableFuture.allOf(futures).join();
        long wall = System.nanoTime() - start;
        return new BenchResult(name, tasks, wall, samples, peakThreads.get(), snapshot.usedMemoryBytes());
    }

    private void warmup(ExecutorService pool, int n) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(n);
        for (int i = 0; i < n; i++) {
            pool.execute(() -> {
                block();
                latch.countDown();
            });
        }
        latch.await(30, TimeUnit.SECONDS);
    }

    private void block() {
        try {
            Thread.sleep(delayMs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
