package exp;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public final class IoBufferedVsUnbuffered {
    public static void main(String[] args) throws Exception {
        int n = args.length > 0 ? Integer.parseInt(args[0].replace("_", "")) : 2_000_000;
        System.out.printf(Locale.ROOT, "writing %d single-byte writes (not JMH; disk-dependent)%n", n);
        List<Long> raw = new ArrayList<>();
        List<Long> buf = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            raw.add(timeWrite(n, false));
            buf.add(timeWrite(n, true));
        }
        print("unbuffered FileOutputStream", raw);
        print("BufferedOutputStream", buf);
    }

    private static long timeWrite(int n, boolean buffered) throws Exception {
        File f = File.createTempFile("io-exp-", ".bin");
        try {
            long t0 = System.nanoTime();
            try (OutputStream out = buffered
                    ? new BufferedOutputStream(new FileOutputStream(f))
                    : new FileOutputStream(f)) {
                for (int i = 0; i < n; i++) out.write(1);
            }
            return System.nanoTime() - t0;
        } finally {
            //noinspection ResultOfMethodCallIgnored
            f.delete();
        }
    }

    private static void print(String label, List<Long> ns) {
        Collections.sort(ns);
        System.out.printf(Locale.ROOT, "%s median=%.2fms (min=%.2f max=%.2f)%n",
                label, ns.get(ns.size() / 2) / 1e6, ns.getFirst() / 1e6, ns.getLast() / 1e6);
    }
}
