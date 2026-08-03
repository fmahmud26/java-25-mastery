package payment.domain;

import java.util.Optional;

public interface PaymentRepository {
    Optional<Payment> findByIdempotencyKey(String key);

    Optional<Payment> findById(String paymentId);

    void save(Payment payment);

    void update(Payment payment);
}
