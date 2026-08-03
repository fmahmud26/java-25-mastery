package mt;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

public record Order(
        long id,
        String customerAccount,
        String merchantAccount,
        String sku,
        int quantity,
        BigDecimal amount
) {
    private static final AtomicLong SEQ = new AtomicLong(1);

    public static Order create(String customer, String merchant, String sku, int qty, BigDecimal amount) {
        return new Order(SEQ.getAndIncrement(), customer, merchant, sku, qty, amount);
    }
}
