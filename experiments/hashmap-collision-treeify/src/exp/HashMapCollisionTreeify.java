package exp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public final class HashMapCollisionTreeify {
    record BadKey(int id) {
        @Override public int hashCode() { return 42; } // intentional collision
        @Override public boolean equals(Object o) {
            return o instanceof BadKey b && b.id == id;
        }
    }

    public static void main(String[] args) {
        int n = args.length > 0 ? Integer.parseInt(args[0].replace("_", "")) : 20_000;
        System.out.printf(Locale.ROOT, "n=%d (teaching microbench, not JMH)%n", n);

        Map<BadKey, Integer> bad = new HashMap<>();
        Map<Integer, Integer> good = new HashMap<>();
        for (int i = 0; i < n; i++) {
            bad.put(new BadKey(i), i);
            good.put(i, i);
        }

        List<Long> badGets = new ArrayList<>();
        List<Long> goodGets = new ArrayList<>();
        for (int r = 0; r < 5; r++) {
            badGets.add(timeGetsBad(bad, n));
            goodGets.add(timeGetsGood(good, n));
        }
        print("pathological hash get×n", badGets);
        print("normal Integer get×n", goodGets);
    }

    private static long timeGetsBad(Map<BadKey, Integer> map, int n) {
        long t0 = System.nanoTime();
        int sink = 0;
        for (int i = 0; i < n; i++) sink ^= Objects.requireNonNull(map.get(new BadKey(i)));
        if (sink == 42) System.out.print(""); // keep side effect
        return System.nanoTime() - t0;
    }

    private static long timeGetsGood(Map<Integer, Integer> map, int n) {
        long t0 = System.nanoTime();
        int sink = 0;
        for (int i = 0; i < n; i++) sink ^= Objects.requireNonNull(map.get(i));
        if (sink == 42) System.out.print("");
        return System.nanoTime() - t0;
    }

    private static void print(String label, List<Long> ns) {
        Collections.sort(ns);
        System.out.printf(Locale.ROOT, "%s: median=%.2fms (min=%.2f max=%.2f)%n",
                label, ns.get(ns.size() / 2) / 1e6, ns.get(0) / 1e6, ns.get(ns.size() - 1) / 1e6);
    }
}
