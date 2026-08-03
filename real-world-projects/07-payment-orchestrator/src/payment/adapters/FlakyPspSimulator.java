package payment.adapters;

import payment.domain.PspClient;
import payment.domain.PspOutcome;
import payment.domain.PspResult;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Flaky PSP: first call for a key may timeout-unknown while "capturing" server-side;
 * second call with same key returns CAPTURED (provider idempotency).
 */
public final class FlakyPspSimulator implements PspClient {
    public enum Mode { ALWAYS_CAPTURE, ALWAYS_DECLINE, TIMEOUT_THEN_CAPTURE }

    private final Mode mode;
    private final ConcurrentHashMap<String, Boolean> captured = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, AtomicInteger> attempts = new ConcurrentHashMap<>();

    public FlakyPspSimulator(Mode mode) {
        this.mode = mode;
    }

    @Override
    public PspResult charge(String providerIdempotencyKey, long amountCents) {
        int n = attempts.computeIfAbsent(providerIdempotencyKey, k -> new AtomicInteger()).incrementAndGet();
        return switch (mode) {
            case ALWAYS_CAPTURE -> {
                captured.put(providerIdempotencyKey, true);
                yield new PspResult(PspOutcome.CAPTURED, "psp-" + providerIdempotencyKey);
            }
            case ALWAYS_DECLINE -> new PspResult(PspOutcome.DECLINED, null);
            case TIMEOUT_THEN_CAPTURE -> {
                if (n == 1) {
                    captured.put(providerIdempotencyKey, true); // captured but client saw timeout
                    yield new PspResult(PspOutcome.UNKNOWN_TIMEOUT, null);
                }
                yield new PspResult(PspOutcome.CAPTURED, "psp-" + providerIdempotencyKey);
            }
        };
    }

    public boolean providerThinksCaptured(String key) {
        return captured.containsKey(key);
    }
}
