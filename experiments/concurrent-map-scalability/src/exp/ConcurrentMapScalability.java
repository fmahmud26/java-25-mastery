package exp;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

public final class ConcurrentMapScalability {
    public static void main(String[] args) throws Exception {
        int threads = args.length > 0 ? Integer.parseInt(args[0]) : 8;
        int seconds = args.length > 1 ? Integer.parseInt(args[1]) : 2;
        System.out.printf(Locale.ROOT, "threads=%d seconds=%d (teaching harness, not JMH)%n", threads, seconds);

        bench("ConcurrentHashMap", new ConcurrentHashMap<>(), threads, seconds);
        bench("synchronized HashMap", Collections.synchronizedMap(new HashMap<>()), threads, seconds);
    }

    private static void bench(String name, Map<Integer, Long> map, int threads, int seconds) throws Exception {
        LongAdder ops = new LongAdder();
        ExecutorService pool = Executors.newVirtualThreadPerTaskExecutor();
        long end = System.nanoTime() + TimeUnit.SECONDS.toNanos(seconds);
        for (int t = 0; t < threads; t++) {
            pool.submit(() -> {
                ThreadLocalRandom rnd = ThreadLocalRandom.current();
                while (System.nanoTime() < end) {
                    int k = rnd.nextInt(1024);
                    map.merge(k, 1L, Long::sum);
                    ops.increment();
                }
            });
        }
        pool.close();
        double opsPerSec = ops.sum() / (double) seconds;
        System.out.printf(Locale.ROOT, "%s: ops/s≈%.0f (local only)%n", name, opsPerSec);
    }
}
