package exp;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public final class StreamLazyShortcircuit {
    public static void main(String[] args) {
        int n = 1_000_000;
        int target = 1_000;
        AtomicInteger peeks = new AtomicInteger();
        int found = IntStream.range(0, n)
                .peek(i -> peeks.incrementAndGet())
                .filter(i -> i == target)
                .findFirst()
                .orElseThrow();
        System.out.printf(Locale.ROOT,
                "found=%d peeks=%d n=%d (expect peeks ~= target+1, not n)%n",
                found, peeks.get(), n);
    }
}
