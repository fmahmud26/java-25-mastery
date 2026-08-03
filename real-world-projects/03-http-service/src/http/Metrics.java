package http;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/** Simple process-wide counters for the demo server. */
public final class Metrics {
    private final LongAdder requests = new LongAdder();
    private final LongAdder errors = new LongAdder();
    private final LongAdder bytesOut = new LongAdder();
    private final AtomicLong inFlight = new AtomicLong();
    private final AtomicLong maxInFlight = new AtomicLong();

    public void onStart() {
        long now = inFlight.incrementAndGet();
        maxInFlight.updateAndGet(prev -> Math.max(prev, now));
    }

    public void onSuccess(int bytes) {
        requests.increment();
        bytesOut.add(bytes);
        inFlight.decrementAndGet();
    }

    public void onError() {
        errors.increment();
        inFlight.decrementAndGet();
    }

    public String snapshot() {
        return """
                requests=%d
                errors=%d
                bytesOut=%d
                inFlight=%d
                maxInFlight=%d
                """.formatted(
                requests.sum(),
                errors.sum(),
                bytesOut.sum(),
                inFlight.get(),
                maxInFlight.get());
    }
}
