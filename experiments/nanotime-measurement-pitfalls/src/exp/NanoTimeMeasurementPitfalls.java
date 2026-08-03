package exp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public final class NanoTimeMeasurementPitfalls {
    public static void main(String[] args) {
        System.out.println("WARNING: This demonstrates measurement noise. Not a real benchmark. Not JMH.");

        System.out.println("-- single-shot (bad) --");
        for (int i = 0; i < 5; i++) {
            long t0 = System.nanoTime();
            sink = crunch(i);
            long dt = System.nanoTime() - t0;
            System.out.printf(Locale.ROOT, "shot %d: %d ns%n", i, dt);
        }

        System.out.println("-- empty timed region (timer noise) --");
        for (int i = 0; i < 5; i++) {
            long t0 = System.nanoTime();
            long dt = System.nanoTime() - t0;
            System.out.printf(Locale.ROOT, "empty %d: %d ns%n", i, dt);
        }

        System.out.println("-- warmed batch medians (still not JMH) --");
        for (int i = 0; i < 100; i++) sink ^= crunch(i);
        List<Long> samples = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            long t0 = System.nanoTime();
            int local = 0;
            for (int j = 0; j < 10_000; j++) local ^= crunch(j);
            sink ^= local;
            samples.add(System.nanoTime() - t0);
        }
        Collections.sort(samples);
        System.out.printf(Locale.ROOT, "batch median=%d ns min=%d max=%d (spread shows noise)%n",
                samples.get(samples.size() / 2), samples.getFirst(), samples.getLast());
        System.out.println("sink=" + sink);
        System.out.println("Use JMH + JFR for decisions. Do not quote these numbers externally.");
    }

    private static int sink;

    private static int crunch(int x) {
        int v = x;
        for (int i = 0; i < 8; i++) v = (v * 31) ^ (v >>> 3);
        return v;
    }
}
