package mt;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Composes inventory reservation + payment using CompletableFuture on a mixed executor setup.
 */
public final class OrderProcessor {
    private final InventoryService inventory;
    private final AccountLedger ledger;
    private final ExecutorService virtualWorkers;
    private final ExecutorService platformPool;
    private final ConcurrentHashMap<Long, OrderStatus> statuses = new ConcurrentHashMap<>();
    private final AtomicLong completed = new AtomicLong();
    private final AtomicLong failed = new AtomicLong();

    public OrderProcessor(InventoryService inventory,
                          AccountLedger ledger,
                          ExecutorService virtualWorkers,
                          ExecutorService platformPool) {
        this.inventory = inventory;
        this.ledger = ledger;
        this.virtualWorkers = virtualWorkers;
        this.platformPool = platformPool;
    }

    public ConcurrentHashMap<Long, OrderStatus> statuses() {
        return statuses;
    }

    public long completedCount() { return completed.get(); }
    public long failedCount() { return failed.get(); }

    public CompletableFuture<OrderStatus> processAsync(Order order) {
        statuses.put(order.id(), OrderStatus.NEW);

        return CompletableFuture.supplyAsync(
                        () -> inventory.reserve(order.sku(), order.quantity()),
                        virtualWorkers)
                .thenComposeAsync(reserved -> {
                    if (!reserved) {
                        return fail(order, false);
                    }
                    statuses.put(order.id(), OrderStatus.RESERVED);

                    return CompletableFuture.supplyAsync(
                                    () -> ledger.transfer(
                                            order.customerAccount(),
                                            order.merchantAccount(),
                                            order.amount()),
                                    virtualWorkers)
                            .thenApplyAsync(paid -> {
                                if (!paid) {
                                    inventory.release(order.sku(), order.quantity());
                                    statuses.put(order.id(), OrderStatus.FAILED);
                                    failed.incrementAndGet();
                                    return OrderStatus.FAILED;
                                }
                                statuses.put(order.id(), OrderStatus.PAID);
                                reconcile(order);
                                statuses.put(order.id(), OrderStatus.COMPLETED);
                                completed.incrementAndGet();
                                return OrderStatus.COMPLETED;
                            }, platformPool);
                }, virtualWorkers)
                .exceptionally(ex -> {
                    // Best-effort compensating release if we crashed after a successful reserve.
                    if (statuses.get(order.id()) == OrderStatus.RESERVED
                            || statuses.get(order.id()) == OrderStatus.PAID) {
                        inventory.release(order.sku(), order.quantity());
                    }
                    statuses.put(order.id(), OrderStatus.FAILED);
                    failed.incrementAndGet();
                    return OrderStatus.FAILED;
                });
    }

    private CompletableFuture<OrderStatus> fail(Order order, boolean releaseInventory) {
        if (releaseInventory) {
            inventory.release(order.sku(), order.quantity());
        }
        statuses.put(order.id(), OrderStatus.FAILED);
        failed.incrementAndGet();
        return CompletableFuture.completedFuture(OrderStatus.FAILED);
    }

    private void reconcile(Order order) {
        long checksum = order.id() ^ order.quantity() ^ order.amount().unscaledValue().longValue();
        if (checksum == Long.MIN_VALUE) {
            throw new AssertionError();
        }
    }

    public void shutdown() throws InterruptedException {
        virtualWorkers.shutdown();
        platformPool.shutdown();
        virtualWorkers.awaitTermination(5, TimeUnit.SECONDS);
        platformPool.awaitTermination(5, TimeUnit.SECONDS);
    }
}
