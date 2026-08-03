package exp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class HashMapResizeCost {
    public static void main(String[] args) {
        int n = args.length > 0 ? Integer.parseInt(args[0].replace("_", "")) : 1_000_000;
        int warmup = 3;
        int measured = 5;
        System.out.printf(Locale.ROOT, "n=%d warmup=%d measured=%d (not JMH)%n", n, warmup, measured);

        for (int i = 0; i < warmup; i++) {
            fill(new HashMap<>(), n);
            fill(new HashMap<>(n), n);
        }

        List<Long> def = new ArrayList<>();
        List<Long> sized = new ArrayList<>();
        for (int i = 0; i < measured; i++) {
            def.add(time(() -> fill(new HashMap<>(), n)));
            sized.add(time(() -> fill(new HashMap<>(n), n)));
        }
        print("default HashMap", def);
        print("pre-sized HashMap(n)", sized);
        System.out.println("Compare medians only on this machine. Do not publish as universal benchmark.");
    }

    private static void fill(Map<Integer, Integer> map, int n) {
        for (int i = 0; i < n; i++) map.put(i, i);
    }

    private static long time(Runnable r) {
        long t0 = System.nanoTime();
        r.run();
        return System.nanoTime() - t0;
    }

    private static void print(String label, List<Long> samplesNs) {
        Collections.sort(samplesNs);
        long med = samplesNs.get(samplesNs.size() / 2);
        long min = samplesNs.get(0);
        long max = samplesNs.get(samplesNs.size() - 1);
        System.out.printf(Locale.ROOT, "%s: min=%.2fms median=%.2fms max=%.2fms%n",
                label, min / 1e6, med / 1e6, max / 1e6);
    }
}
