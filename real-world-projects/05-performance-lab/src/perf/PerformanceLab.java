package perf;

/**
 * Microbench entry point — Platform vs Virtual vs CompletableFuture.
 *
 * <p>Not JMH-grade; useful for demos and intuition about virtual threads under blocking I/O.
 */
public final class PerformanceLab {
    public static void main(String[] args) throws Exception {
        int tasks = 1000;
        long delayMs = 5;
        int platformThreads = 32;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--tasks" -> tasks = Integer.parseInt(args[++i]);
                case "--delay-ms" -> delayMs = Long.parseLong(args[++i]);
                case "--platform-threads" -> platformThreads = Integer.parseInt(args[++i]);
                case "-h", "--help" -> {
                    System.out.println("""
                            Usage: PerformanceLab [--tasks N] [--delay-ms N] [--platform-threads N]

                            Compares Platform Threads vs Virtual Threads vs CompletableFuture
                            for a blocking sleep workload. Microbench only — use JMH for real numbers.
                            """);
                    return;
                }
                default -> throw new IllegalArgumentException("Unknown arg: " + args[i]);
            }
        }

        System.out.println("Performance Lab (microbench — not JMH)");
        System.out.printf("tasks=%d delayMs=%d platformThreads=%d processors=%d%n%n",
                tasks, delayMs, platformThreads, Runtime.getRuntime().availableProcessors());

        var bench = new WorkloadBench(tasks, delayMs, platformThreads);

        BenchResult platform = bench.platformThreads();
        System.out.print(platform.render());

        BenchResult virtual = bench.virtualThreads();
        System.out.print(virtual.render());

        BenchResult cf = bench.completableFutures();
        System.out.print(cf.render());

        System.out.println("Notes:");
        System.out.println("- Blocking I/O favors virtual threads (cheap parking, high concurrency).");
        System.out.println("- Platform pools cap concurrency at pool size → higher queueing latency.");
        System.out.println("- ThreadMXBean counts may under-represent virtual threads depending on JVM.");
        System.out.println("- For production measurement, use JMH with proper warmup/forks.");
    }

    private PerformanceLab() {}
}
