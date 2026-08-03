package mt;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Demo entry: virtual-thread workers + platform ExecutorService + CompletableFuture pipeline.
 */
public final class OrderProcessorApp {
    public static void main(String[] args) throws Exception {
        int orderCount = 150;
        int platformThreads = Math.max(2, Runtime.getRuntime().availableProcessors());

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--orders" -> orderCount = Integer.parseInt(args[++i]);
                case "--platform-threads" -> platformThreads = Integer.parseInt(args[++i]);
                case "-h", "--help" -> {
                    System.out.println("""
                            Usage: OrderProcessorApp [--orders N] [--platform-threads N]
                            Demonstrates ExecutorService, CompletableFuture, ConcurrentHashMap,
                            ReentrantLock, Atomic*, and Virtual Threads.
                            """);
                    return;
                }
                default -> throw new IllegalArgumentException("Unknown arg: " + args[i]);
            }
        }

        var inventory = new InventoryService()
                .seed("SKU-A", 80)
                .seed("SKU-B", 60)
                .seed("SKU-C", 40);

        var ledger = new AccountLedger()
                .seed("cust-1", new BigDecimal("5000.00"))
                .seed("cust-2", new BigDecimal("5000.00"))
                .seed("merchant", new BigDecimal("0.00"));

        ExecutorService virtualWorkers = Executors.newVirtualThreadPerTaskExecutor();
        ThreadFactory platformFactory = Thread.ofPlatform()
                .name("ledger-pool-", 0)
                .factory();
        ExecutorService platformPool = Executors.newFixedThreadPool(platformThreads, platformFactory);

        var processor = new OrderProcessor(inventory, ledger, virtualWorkers, platformPool);

        System.out.printf("Submitting %d orders (VT workers + %d platform threads)...%n",
                orderCount, platformThreads);

        long start = System.nanoTime();
        List<CompletableFuture<OrderStatus>> futures = new ArrayList<>(orderCount);
        String[] skus = {"SKU-A", "SKU-B", "SKU-C"};
        String[] customers = {"cust-1", "cust-2"};

        for (int i = 0; i < orderCount; i++) {
            Order order = Order.create(
                    customers[i % customers.length],
                    "merchant",
                    skus[i % skus.length],
                    1,
                    new BigDecimal("19.99"));
            futures.add(processor.processAsync(order));
        }

        CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new)).join();
        double ms = (System.nanoTime() - start) / 1_000_000.0;

        System.out.println();
        System.out.println("=== Results ===");
        System.out.printf("Completed : %d%n", processor.completedCount());
        System.out.printf("Failed    : %d%n", processor.failedCount());
        System.out.printf("Elapsed   : %.2f ms%n", ms);
        System.out.println("Inventory : " + formatInventory(inventory));
        System.out.println("Balances  : " + ledger.balancesView());
        System.out.printf("Status map size (ConcurrentHashMap): %d%n", processor.statuses().size());

        processor.shutdown();
    }

    private static String formatInventory(InventoryService inventory) {
        return "SKU-A=" + inventory.available("SKU-A")
                + ", SKU-B=" + inventory.available("SKU-B")
                + ", SKU-C=" + inventory.available("SKU-C");
    }

    private OrderProcessorApp() {}
}
