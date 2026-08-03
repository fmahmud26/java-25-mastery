package exp;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public final class EscapeOrNotDemo {
    static final class Point {
        final int x, y;
        Point(int x, int y) { this.x = x; this.y = y; }
        int manh() { return Math.abs(x) + Math.abs(y); }
    }

    public static void main(String[] args) {
        long windowMs = 1500;
        System.out.println("Warmup...");
        for (int i = 0; i < 5; i++) {
            workNonEscaping(200_000);
            workEscaping(200_000);
        }
        System.out.println("Measured window ms=" + windowMs + " (teaching; not JMH)");
        long nonEsc = runWindow("non-escaping", windowMs, true);
        long esc = runWindow("escaping-list", windowMs, false);
        System.out.printf(Locale.ROOT, "ratio nonEsc/esc iters ≈ %.2f (local only)%n",
                nonEsc / (double) Math.max(1, esc));
    }

    private static long runWindow(String label, long windowMs, boolean nonEscaping) {
        long end = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(windowMs);
        long iters = 0;
        int sink = 0;
        while (System.nanoTime() < end) {
            if (nonEscaping) sink ^= workNonEscaping(256);
            else sink ^= workEscaping(256);
            iters++;
        }
        System.out.printf(Locale.ROOT, "%s: batches=%d sink=%d%n", label, iters, sink);
        return iters;
    }

    private static int workNonEscaping(int n) {
        int s = 0;
        for (int i = 0; i < n; i++) {
            Point p = new Point(i, -i);
            s += p.manh();
        }
        return s;
    }

    private static int workEscaping(int n) {
        List<Point> list = new ArrayList<>(n);
        for (int i = 0; i < n; i++) list.add(new Point(i, -i));
        int s = 0;
        for (Point p : list) s += p.manh();
        return s;
    }
}
