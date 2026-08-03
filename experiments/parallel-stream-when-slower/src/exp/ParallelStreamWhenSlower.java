package exp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;

public final class ParallelStreamWhenSlower {
    public static void main(String[] args) {
        System.out.println("Teaching microbench (not JMH). Warmup...");
        for (int i = 0; i < 5; i++) {
            sum(false, 1000);
            sum(true, 1000);
        }
        compare("tiny n=2_000 cheap", 2_000, false);
        compare("large n=20_000_000 cheap", 20_000_000, false);
        compare("large n=2_000_000 heavier", 2_000_000, true);
    }

    private static void compare(String label, int n, boolean heavier) {
        List<Long> seq = new ArrayList<>();
        List<Long> par = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            seq.add(time(() -> sum(false, n, heavier)));
            par.add(time(() -> sum(true, n, heavier)));
        }
        System.out.printf(Locale.ROOT, "%s%n", label);
        print("  sequential", seq);
        print("  parallel  ", par);
    }

    private static long sum(boolean parallel, int n) {
        return sum(parallel, n, false);
    }

    private static long sum(boolean parallel, int n, boolean heavier) {
        IntStream s = IntStream.range(0, n);
        if (parallel) s = s.parallel();
        if (!heavier) return s.sum();
        return s.map(ParallelStreamWhenSlower::crunch).asLongStream().sum();
    }

    private static int crunch(int x) {
        int v = x;
        for (int i = 0; i < 16; i++) v = (v * 1664525) + 1013904223;
        return v;
    }

    private static long time(Runnable r) {
        long t0 = System.nanoTime();
        r.run();
        return System.nanoTime() - t0;
    }

    private static void print(String label, List<Long> ns) {
        Collections.sort(ns);
        System.out.printf(Locale.ROOT, "%s median=%.2fms%n", label, ns.get(ns.size() / 2) / 1e6);
    }
}
