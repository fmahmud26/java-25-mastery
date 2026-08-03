package mt;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Inventory backed by ConcurrentHashMap; stock levels are AtomicInteger.
 */
public final class InventoryService {
    private final ConcurrentHashMap<String, AtomicInteger> stock = new ConcurrentHashMap<>();

    public InventoryService seed(String sku, int qty) {
        stock.put(sku, new AtomicInteger(qty));
        return this;
    }

    /** Returns true if reservation succeeded. */
    public boolean reserve(String sku, int qty) {
        AtomicInteger level = stock.get(sku);
        if (level == null) {
            return false;
        }
        while (true) {
            int current = level.get();
            if (current < qty) {
                return false;
            }
            if (level.compareAndSet(current, current - qty)) {
                return true;
            }
        }
    }

    public void release(String sku, int qty) {
        stock.computeIfAbsent(sku, _ -> new AtomicInteger(0)).addAndGet(qty);
    }

    public int available(String sku) {
        AtomicInteger level = stock.get(sku);
        return level == null ? 0 : level.get();
    }

    public ConcurrentHashMap<String, AtomicInteger> snapshot() {
        return stock;
    }
}
