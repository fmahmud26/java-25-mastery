package payment.domain;

import java.time.Instant;
import java.util.Objects;

public final class Payment {
    private final String paymentId;
    private final String idempotencyKey;
    private final long amountCents;
    private final String merchantId;
    private PaymentStatus status;
    private final Instant createdAt;
    private Instant updatedAt;

    public Payment(
            String paymentId,
            String idempotencyKey,
            long amountCents,
            String merchantId,
            PaymentStatus status,
            Instant createdAt) {
        this.paymentId = Objects.requireNonNull(paymentId);
        this.idempotencyKey = Objects.requireNonNull(idempotencyKey);
        this.amountCents = amountCents;
        this.merchantId = Objects.requireNonNull(merchantId);
        this.status = Objects.requireNonNull(status);
        this.createdAt = Objects.requireNonNull(createdAt);
        this.updatedAt = createdAt;
    }

    public String paymentId() { return paymentId; }
    public String idempotencyKey() { return idempotencyKey; }
    public long amountCents() { return amountCents; }
    public String merchantId() { return merchantId; }
    public PaymentStatus status() { return status; }
    public Instant createdAt() { return createdAt; }
    public Instant updatedAt() { return updatedAt; }

    public void transitionTo(PaymentStatus next, Instant now) {
        if (!isAllowed(status, next)) {
            throw new IllegalStateException(status + " -> " + next);
        }
        this.status = next;
        this.updatedAt = now;
    }

    private static boolean isAllowed(PaymentStatus from, PaymentStatus to) {
        if (from == to) return true;
        return switch (from) {
            case INITIATED -> to == PaymentStatus.PENDING || to == PaymentStatus.CAPTURED || to == PaymentStatus.FAILED;
            case PENDING -> to == PaymentStatus.CAPTURED || to == PaymentStatus.FAILED;
            case CAPTURED, FAILED -> false;
        };
    }
}
