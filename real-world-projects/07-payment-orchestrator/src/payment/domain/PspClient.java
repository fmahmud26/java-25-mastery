package payment.domain;

public interface PspClient {
    PspResult charge(String providerIdempotencyKey, long amountCents);
}
