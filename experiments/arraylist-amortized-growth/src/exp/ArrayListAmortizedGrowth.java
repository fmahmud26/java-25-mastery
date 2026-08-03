package exp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public final class ArrayListAmortizedGrowth {
    public static void main(String[] args) {
        int n = args.length > 0 ? Integer.parseInt(args[0].replace("_", "")) : 2_000_000;
        for (int i = 0; i < 3; i++) {
            fill(new ArrayList<>(), n);
            fill(new ArrayList<>(n), n);
        }
        List<Long> a = new ArrayList<>();
        List<Long> b = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            a.add(time(() -> fill(new ArrayList<>(), n)));
            b.add(time(() -> fill(new ArrayList<>(n), n)));
        }
        print("default ArrayList", a);
        print("ArrayList(n)", b);
        System.out.println("Teaching microbench only — not JMH.");
    }

    private static void fill(List<Integer> list, int n) {
        for (int i = 0; i < n; i++) list.add(i);
    }

    private static long time(Runnable r) {
        long t0 = System.nanoTime();
        r.run();
        return System.nanoTime() - t0;
    }

    private static void print(String label, List<Long> ns) {
        Collections.sort(ns);
        System.out.printf(Locale.ROOT, "%s median=%.2fms (min=%.2f max=%.2f)%n",
                label, ns.get(ns.size() / 2) / 1e6, ns.getFirst() / 1e6, ns.getLast() / 1e6);
    }
}
