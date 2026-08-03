package exp;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public final class VirtualVsPlatformBlocking {
    public static void main(String[] args) throws Exception {
        int tasks = args.length > 0 ? Integer.parseInt(args[0]) : 2_000;
        int delayMs = args.length > 1 ? Integer.parseInt(args[1]) : 10;
        int pool = args.length > 2 ? Integer.parseInt(args[2]) : 16;
        System.out.printf(Locale.ROOT,
                "tasks=%d delayMs=%d platformPool=%d (teaching; not JMH)%n", tasks, delayMs, pool);

        try (ExecutorService platform = Executors.newFixedThreadPool(pool);
             ExecutorService virtual = Executors.newVirtualThreadPerTaskExecutor()) {
            System.out.printf(Locale.ROOT, "platform pool wallMs=%d%n", wall(platform, tasks, delayMs));
            System.out.printf(Locale.ROOT, "virtual per-task wallMs=%d%n", wall(virtual, tasks, delayMs));
        }
    }

    private static long wall(ExecutorService exec, int tasks, int delayMs) throws Exception {
        long t0 = System.nanoTime();
        List<Future<?>> futures = new ArrayList<>(tasks);
        for (int i = 0; i < tasks; i++) {
            futures.add(exec.submit(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(delayMs);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }));
        }
        for (Future<?> f : futures) f.get();
        return (System.nanoTime() - t0) / 1_000_000L;
    }
}
