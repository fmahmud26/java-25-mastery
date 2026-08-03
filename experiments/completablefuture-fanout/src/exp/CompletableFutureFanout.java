package exp;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public final class CompletableFutureFanout {
    public static void main(String[] args) throws Exception {
        try (ExecutorService vt = Executors.newVirtualThreadPerTaskExecutor()) {
            long seq = sequential();
            long async = asyncFanout(vt);
            System.out.printf(Locale.ROOT, "sequential wallMs=%d%n", seq);
            System.out.printf(Locale.ROOT, "async fan-out wallMs=%d (teaching; not JMH)%n", async);
        }
    }

    private static long sequential() throws Exception {
        long t0 = System.nanoTime();
        sleep(120);
        sleep(120);
        sleep(120);
        return (System.nanoTime() - t0) / 1_000_000L;
    }

    private static long asyncFanout(ExecutorService exec) throws Exception {
        long t0 = System.nanoTime();
        CompletableFuture<Void> a = CompletableFuture.runAsync(() -> sleep(120), exec);
        CompletableFuture<Void> b = CompletableFuture.runAsync(() -> sleep(120), exec);
        CompletableFuture<Void> c = CompletableFuture.runAsync(() -> sleep(120), exec);
        CompletableFuture.allOf(a, b, c).get(5, TimeUnit.SECONDS);
        return (System.nanoTime() - t0) / 1_000_000L;
    }

    private static void sleep(long ms) {
        try {
            TimeUnit.MILLISECONDS.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
