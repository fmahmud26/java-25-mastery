package perf;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public final class RuntimeSnapshot {
    private final ThreadMXBean threads = ManagementFactory.getThreadMXBean();

    public int threadCount() {
        return threads.getThreadCount();
    }

    public long usedMemoryBytes() {
        Runtime rt = Runtime.getRuntime();
        return rt.totalMemory() - rt.freeMemory();
    }

    public void hintGc() {
        System.gc();
    }
}
