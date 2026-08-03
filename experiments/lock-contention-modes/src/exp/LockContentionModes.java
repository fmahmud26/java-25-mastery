package exp;

import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.ReentrantLock;

public final class LockContentionModes {
    public static void main(String[] args) throws Exception {
        int threads = args.length > 0 ? Integer.parseInt(args[0]) : 8;
        int seconds = args.length > 1 ? Integer.parseInt(args[1]) : 2;
        System.out.printf(Locale.ROOT, "threads=%d seconds=%d (not JMH)%n", threads, seconds);
        System.out.printf(Locale.ROOT, "synchronized ops/s≈%.0f%n", benchSync(threads, seconds));
        System.out.printf(Locale.ROOT, "ReentrantLock ops/s≈%.0f%n", benchLock(threads, seconds));
        System.out.printf(Locale.ROOT, "LongAdder ops/s≈%.0f%n", benchAdder(threads, seconds));
    }

    private static double benchSync(int threads, int seconds) throws Exception {
        Object lock = new Object();
        long[] cell = {0};
        LongAdder ops = new LongAdder();
        run(threads, seconds, () -> {
            synchronized (lock) { cell[0]++; }
            ops.increment();
        });
        return ops.sum() / (double) seconds;
    }

    private static double benchLock(int threads, int seconds) throws Exception {
        ReentrantLock lock = new ReentrantLock();
        long[] cell = {0};
        LongAdder ops = new LongAdder();
        run(threads, seconds, () -> {
            lock.lock();
            try { cell[0]++; }
            finally { lock.unlock(); }
            ops.increment();
        });
        return ops.sum() / (double) seconds;
    }

    private static double benchAdder(int threads, int seconds) throws Exception {
        LongAdder cell = new LongAdder();
        LongAdder ops = new LongAdder();
        run(threads, seconds, () -> {
            cell.increment();
            ops.increment();
        });
        return ops.sum() / (double) seconds;
    }

    private static void run(int threads, int seconds, Runnable body) throws Exception {
        ExecutorService pool = Executors.newVirtualThreadPerTaskExecutor();
        long end = System.nanoTime() + TimeUnit.SECONDS.toNanos(seconds);
        for (int i = 0; i < threads; i++) {
            pool.submit(() -> {
                while (System.nanoTime() < end) body.run();
            });
        }
        pool.close();
    }
}
