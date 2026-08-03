package perf;

import java.util.Arrays;

public record BenchResult(
        String name,
        int tasks,
        long wallNanos,
        long[] sampleLatenciesNanos,
        int peakThreadCount,
        long usedMemoryBytes
) {
    public double throughputOpsPerSec() {
        if (wallNanos <= 0) {
            return 0;
        }
        return tasks * 1_000_000_000.0 / wallNanos;
    }

    public double avgLatencyMs() {
        if (sampleLatenciesNanos.length == 0) {
            return 0;
        }
        long sum = 0;
        for (long n : sampleLatenciesNanos) {
            sum += n;
        }
        return (sum / (double) sampleLatenciesNanos.length) / 1_000_000.0;
    }

    public double percentileMs(double p) {
        if (sampleLatenciesNanos.length == 0) {
            return 0;
        }
        long[] sorted = sampleLatenciesNanos.clone();
        Arrays.sort(sorted);
        int idx = (int) Math.ceil(p / 100.0 * sorted.length) - 1;
        idx = Math.clamp(idx, 0, sorted.length - 1);
        return sorted[idx] / 1_000_000.0;
    }

    public String render() {
        return """
                --- %s ---
                tasks           : %d
                wall time       : %.2f ms
                throughput      : %.1f ops/s
                latency avg     : %.3f ms
                latency p50     : %.3f ms
                latency p95     : %.3f ms
                latency p99     : %.3f ms
                peak threads    : %d
                used heap       : %.2f MB
                """.formatted(
                name,
                tasks,
                wallNanos / 1_000_000.0,
                throughputOpsPerSec(),
                avgLatencyMs(),
                percentileMs(50),
                percentileMs(95),
                percentileMs(99),
                peakThreadCount,
                usedMemoryBytes / (1024.0 * 1024.0));
    }
}
