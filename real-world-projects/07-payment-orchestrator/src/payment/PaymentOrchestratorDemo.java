package payment;

import payment.adapters.FlakyPspSimulator;
import payment.adapters.InMemoryPaymentRepository;
import payment.application.PaymentService;
import payment.domain.Payment;
import payment.domain.PaymentStatus;

import java.time.Clock;

public final class PaymentOrchestratorDemo {
    public static void main(String[] args) {
        var repo = new InMemoryPaymentRepository();
        var psp = new FlakyPspSimulator(FlakyPspSimulator.Mode.TIMEOUT_THEN_CAPTURE);
        var service = new PaymentService(repo, psp, Clock.systemUTC());

        Payment first = service.charge("key-1", 2599, "m-1");
        System.out.println("first status=" + first.status() + " (expect PENDING)");

        Payment dup = service.charge("key-1", 2599, "m-1");
        System.out.println("dup status=" + dup.status() + " sameId=" + dup.paymentId().equals(first.paymentId()));
        System.out.println("duplicateHits=" + service.duplicateHits() + " charges=" + service.charges());

        // reconcile path: provider already captured
        if (psp.providerThinksCaptured("key-1") && first.status() == PaymentStatus.PENDING) {
            service.applyProviderCaptured(first.paymentId());
        }
        System.out.println("after webhook status=" + repo.findById(first.paymentId()).orElseThrow().status());
    }
}
