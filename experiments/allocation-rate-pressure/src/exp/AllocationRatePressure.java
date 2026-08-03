package exp;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public final class AllocationRatePressure {
    public static void main(String[] args) {
        long windowMs = args.length > 0 ? Long.parseLong(args[0]) : 2000;
        System.out.println("Teaching demo with GC logs — not a published benchmark.");
        run("allocating", windowMs, true);
        run("reuse-buffer", windowMs, false);
    }

    private static void run(String label, long windowMs, boolean allocate) {
        long end = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(windowMs);
        long iters = 0;
        StringBuilder reused = new StringBuilder(64);
        int sink = 0;
        while (System.nanoTime() < end) {
            if (allocate) {
                String s = "id=" + iters + "|payload=" + (iters * 31);
                sink ^= s.hashCode();
            } else {
                reused.setLength(0);
                reused.append("id=").append(iters).append("|payload=").append(iters * 31);
                sink ^= reused.toString().hashCode();
            }
            iters++;
        }
        System.out.printf(Locale.ROOT, "%s: iters=%d sink=%d%n", label, iters, sink);
    }
}
