package exp;

import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

public final class AtomicVsSynchronizedCounter {
    public static void main(String[] args) throws Exception {
        int threads = args.length > 0 ? Integer.parseInt(args[0]) : 8;
        int seconds = args.length > 1 ? Integer.parseInt(args[1]) : 2;
        System.out.printf(Locale.ROOT, "threads=%d seconds=%d (not JMH)%n", threads, seconds);
        System.out.printf(Locale.ROOT, "AtomicInteger ops/s≈%.0f%n", atomic(threads, seconds));
        System.out.printf(Locale.ROOT, "synchronized ops/s≈%.0f%n", sync(threads, seconds));
    }

    private static double atomic(int threads, int seconds) throws Exception {
        AtomicInteger c = new AtomicInteger();
        LongAdder ops = new LongAdder();
        run(threads, seconds, () -> { c.incrementAndGet(); ops.increment(); });
        return ops.sum() / (double) seconds;
    }

    private static double sync(int threads, int seconds) throws Exception {
        int[] c = {0};
        Object lock = new Object();
        LongAdder ops = new LongAdder();
        run(threads, seconds, () -> {
            synchronized (lock) { c[0]++; }
            ops.increment();
        });
        return ops.sum() / (double) seconds;
    }

    private static void run(int threads, int seconds, Runnable r) throws Exception {
        ExecutorService pool = Executors.newVirtualThreadPerTaskExecutor();
        long end = System.nanoTime() + TimeUnit.SECONDS.toNanos(seconds);
        for (int i = 0; i < threads; i++) {
            pool.submit(() -> { while (System.nanoTime() < end) r.run(); });
        }
        pool.close();
    }
}
