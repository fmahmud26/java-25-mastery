package payment;

import payment.adapters.FlakyPspSimulator;
import payment.adapters.InMemoryPaymentRepository;
import payment.application.PaymentService;
import payment.domain.Payment;
import payment.domain.PaymentStatus;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

/** Assertion suite — no Maven/JUnit required. Exit 0 = pass. */
public final class PaymentOrchestratorTests {
    private static int failures = 0;

    public static void main(String[] args) {
        Clock clock = Clock.fixed(Instant.parse("2026-08-03T12:00:00Z"), ZoneOffset.UTC);
        testIdempotentCharge(clock);
        testUnknownTimeoutStaysPending(clock);
        testWebhookCapturesPending(clock);
        testDecline(clock);
        if (failures > 0) {
            System.err.println("FAILED: " + failures);
            System.exit(1);
        }
        System.out.println("OK PaymentOrchestratorTests");
    }

    private static void testIdempotentCharge(Clock clock) {
        var repo = new InMemoryPaymentRepository();
        var psp = new FlakyPspSimulator(FlakyPspSimulator.Mode.ALWAYS_CAPTURE);
        var svc = new PaymentService(repo, psp, clock);
        Payment a = svc.charge("key-1", 1000, "m1");
        Payment b = svc.charge("key-1", 1000, "m1");
        expect("same payment id", a.paymentId().equals(b.paymentId()));
        expect("one logical charge", svc.charges() == 1);
        expect("duplicate recorded", svc.duplicateHits() == 1);
        expect("captured", a.status() == PaymentStatus.CAPTURED);
    }

    private static void testUnknownTimeoutStaysPending(Clock clock) {
        var repo = new InMemoryPaymentRepository();
        var psp = new FlakyPspSimulator(FlakyPspSimulator.Mode.TIMEOUT_THEN_CAPTURE);
        var svc = new PaymentService(repo, psp, clock);
        Payment p = svc.charge("key-to", 500, "m1");
        expect("pending on unknown", p.status() == PaymentStatus.PENDING);
        expect("provider may have captured", psp.providerThinksCaptured("key-to"));
    }

    private static void testWebhookCapturesPending(Clock clock) {
        var repo = new InMemoryPaymentRepository();
        var psp = new FlakyPspSimulator(FlakyPspSimulator.Mode.TIMEOUT_THEN_CAPTURE);
        var svc = new PaymentService(repo, psp, clock);
        Payment p = svc.charge("key-wh", 500, "m1");
        svc.applyProviderCaptured(p.paymentId());
        Payment again = repo.findById(p.paymentId()).orElseThrow();
        expect("webhook capture", again.status() == PaymentStatus.CAPTURED);
        svc.applyProviderCaptured(p.paymentId()); // idempotent
        expect("still captured", repo.findById(p.paymentId()).orElseThrow().status() == PaymentStatus.CAPTURED);
    }

    private static void testDecline(Clock clock) {
        var repo = new InMemoryPaymentRepository();
        var psp = new FlakyPspSimulator(FlakyPspSimulator.Mode.ALWAYS_DECLINE);
        var svc = new PaymentService(repo, psp, clock);
        Payment p = svc.charge("key-d", 100, "m1");
        expect("failed", p.status() == PaymentStatus.FAILED);
    }

    private static void expect(String name, boolean ok) {
        if (!ok) {
            failures++;
            System.err.println("ASSERT " + name);
        }
    }
}
