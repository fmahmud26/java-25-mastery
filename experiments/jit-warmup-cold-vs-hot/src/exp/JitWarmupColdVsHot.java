package exp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public final class JitWarmupColdVsHot {
    public static void main(String[] args) {
        int batch = 50_000;
        System.out.println("Each sample = time for " + batch + " crunch() calls (not JMH)");

        List<Long> cold = new ArrayList<>();
        for (int i = 0; i < 8; i++) cold.add(timeBatch(batch));
        print("early (cold-ish)", cold);

        for (int i = 0; i < 200; i++) timeBatch(batch); // warmup

        List<Long> hot = new ArrayList<>();
        for (int i = 0; i < 8; i++) hot.add(timeBatch(batch));
        print("after warmup", hot);
    }

    private static long timeBatch(int n) {
        long t0 = System.nanoTime();
        int sink = 0;
        for (int i = 0; i < n; i++) sink ^= crunch(i);
        if (sink == 123) System.out.print("");
        return System.nanoTime() - t0;
    }

    private static int crunch(int x) {
        int v = x;
        for (int i = 0; i < 32; i++) {
            v = (v * 1664525) + 1013904223;
            v ^= (v >>> 13);
        }
        return v;
    }

    private static void print(String label, List<Long> ns) {
        Collections.sort(ns);
        System.out.printf(Locale.ROOT, "%s median=%.3fms min=%.3fms max=%.3fms%n",
                label, ns.get(ns.size() / 2) / 1e6, ns.getFirst() / 1e6, ns.getLast() / 1e6);
    }
}
