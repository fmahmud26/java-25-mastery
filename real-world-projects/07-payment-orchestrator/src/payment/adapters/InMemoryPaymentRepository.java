package payment.adapters;

import payment.domain.Payment;
import payment.domain.PaymentRepository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class InMemoryPaymentRepository implements PaymentRepository {
    private final Map<String, Payment> byId = new ConcurrentHashMap<>();
    private final Map<String, String> keyToId = new ConcurrentHashMap<>();

    @Override
    public Optional<Payment> findByIdempotencyKey(String key) {
        String id = keyToId.get(key);
        return id == null ? Optional.empty() : Optional.ofNullable(byId.get(id));
    }

    @Override
    public Optional<Payment> findById(String paymentId) {
        return Optional.ofNullable(byId.get(paymentId));
    }

    @Override
    public void save(Payment payment) {
        if (keyToId.putIfAbsent(payment.idempotencyKey(), payment.paymentId()) != null) {
            throw new IllegalStateException("duplicate idempotency key");
        }
        byId.put(payment.paymentId(), payment);
    }

    @Override
    public void update(Payment payment) {
        byId.put(payment.paymentId(), payment);
    }
}
