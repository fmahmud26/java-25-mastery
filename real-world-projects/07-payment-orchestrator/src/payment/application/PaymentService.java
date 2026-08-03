package payment.application;

import payment.domain.*;

import java.time.Clock;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public final class PaymentService {
    private final PaymentRepository repo;
    private final PspClient psp;
    private final Clock clock;
    private final AtomicInteger duplicateHits = new AtomicInteger();
    private final AtomicInteger charges = new AtomicInteger();

    public PaymentService(PaymentRepository repo, PspClient psp, Clock clock) {
        this.repo = Objects.requireNonNull(repo);
        this.psp = Objects.requireNonNull(psp);
        this.clock = Objects.requireNonNull(clock);
    }

    public Payment charge(String idempotencyKey, long amountCents, String merchantId) {
        Optional<Payment> existing = repo.findByIdempotencyKey(idempotencyKey);
        if (existing.isPresent()) {
            duplicateHits.incrementAndGet();
            return existing.get();
        }

        Payment payment = new Payment(
                UUID.randomUUID().toString(),
                idempotencyKey,
                amountCents,
                merchantId,
                PaymentStatus.INITIATED,
                clock.instant());
        repo.save(payment);
        charges.incrementAndGet();

        payment.transitionTo(PaymentStatus.PENDING, clock.instant());
        repo.update(payment);

        PspResult result = psp.charge(idempotencyKey, amountCents);
        switch (result.outcome()) {
            case CAPTURED -> payment.transitionTo(PaymentStatus.CAPTURED, clock.instant());
            case DECLINED -> payment.transitionTo(PaymentStatus.FAILED, clock.instant());
            case UNKNOWN_TIMEOUT -> { /* stay PENDING for reconcile/webhook */ }
        }
        repo.update(payment);
        return payment;
    }

    public void applyProviderCaptured(String paymentId) {
        Payment p = repo.findById(paymentId).orElseThrow();
        if (p.status() == PaymentStatus.CAPTURED) return;
        p.transitionTo(PaymentStatus.CAPTURED, clock.instant());
        repo.update(p);
    }

    public int duplicateHits() { return duplicateHits.get(); }
    public int charges() { return charges.get(); }
}
