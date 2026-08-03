package exp;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public final class PlatformThreadFootprint {
    public static void main(String[] args) throws Exception {
        int n = args.length > 0 ? Integer.parseInt(args[0].replace("_", "")) : 2_000;
        System.out.printf(Locale.ROOT, "Attempting n=%d sleepers (careful; teaching demo)%n", n);
        tryMode("platform", n, false);
        tryMode("virtual", n, true);
    }

    private static void tryMode(String label, int n, boolean virtual) throws Exception {
        AtomicInteger started = new AtomicInteger();
        CountDownLatch ready = new CountDownLatch(n);
        CountDownLatch release = new CountDownLatch(1);
        List<Thread> threads = new ArrayList<>(n);
        long t0 = System.nanoTime();
        try {
            for (int i = 0; i < n; i++) {
                Thread.Builder b = virtual ? Thread.ofVirtual() : Thread.ofPlatform();
                Thread th = b.unstarted(() -> {
                    started.incrementAndGet();
                    ready.countDown();
                    try {
                        release.await();
                        TimeUnit.MILLISECONDS.sleep(50);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
                threads.add(th);
                th.start();
            }
            boolean ok = ready.await(60, TimeUnit.SECONDS);
            long ms = (System.nanoTime() - t0) / 1_000_000L;
            System.out.printf(Locale.ROOT,
                    "%s: started=%d/%d ready=%s startWallMs=%d%n",
                    label, started.get(), n, ok, ms);
        } catch (Throwable e) {
            System.out.printf(Locale.ROOT, "%s: FAILED after started≈%d (%s)%n",
                    label, started.get(), e);
        } finally {
            release.countDown();
            for (Thread th : threads) {
                try { th.join(1000); } catch (InterruptedException ignored) { Thread.currentThread().interrupt(); }
            }
        }
    }
}
