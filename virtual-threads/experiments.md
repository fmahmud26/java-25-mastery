# Performance Experiments (Virtual Threads)

Run locally / lab. Record methodology — don’t treat toy numbers as production SLOs.

## Safety

Cap concurrency; use timeouts; don’t DDoS real vendors. Prefer localhost mock servers.

---

## Experiment A — Memory: Platform vs Virtual Blocked Threads

**Hypothesis:** N blocked VTs use far less RSS than N blocked platform threads.

```java
static void parkN(int n, boolean virtual) throws Exception {
    var latch = new CountDownLatch(1);
    var started = new CountDownLatch(n);
    for (int i = 0; i < n; i++) {
        Runnable r = () -> {
            started.countDown();
            try { latch.await(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        };
        if (virtual) Thread.ofVirtual().start(r);
        else Thread.ofPlatform().start(r);
    }
    started.await();
    System.out.println("All parked. Measure RSS/heap then latch.countDown()");
    Thread.sleep(60_000);
    latch.countDown();
}
```

Compare n=1_000 / 10_000 (platform may fail earlier). Capture `ps` RSS + heap dump metrics.

---

## Experiment B — Throughput Under Blocking I/O

**Hypothesis:** For sleep/block-dominated work, VT executor sustains higher concurrency than fixed platform pool sized to CPU.

Mock: `Thread.sleep(100)` or localhost HTTP 100 ms delay.

| Executor | Concurrency | Measure |
|----------|-------------|---------|
| Fixed 200 platform | burst 5K | completed/s, p99, failures |
| VT per task | burst 5K | same |

Expect VT higher completed/s until mock saturates.

---

## Experiment C — Pool Exhaustion Unchanged

**Hypothesis:** With Hikari max=10 and query sleep 50 ms, QPS ≈ 10/0.05 = 200 regardless of VT vs platform (once platform pool ≥ 10 waiters).

Prove VT ≠ more DB throughput.

---

## Experiment D — Pinning / Lock Over I/O (behavioral)

Hold `ReentrantLock` across `Thread.sleep` / HTTP; measure carrier utilization and latency vs lock released before I/O.

On Java 25, `synchronized` generally does **not** pin (JEP 491); still teach **don’t hold locks across I/O**.

---

## Experiment E — Stampede Without Bulkhead

VT flood to mock rate-limited server (returns 429 after 100 concurrent). Compare:

1. Unbounded VT  
2. `Semaphore(100)` around client  

Measure success rate and peer load.

---

## Experiment F — CPU-Bound Regression

Submit `n` pure CPU tasks on VT-per-task vs `newFixedThreadPool(Runtime.availableProcessors())`.

Expect platform pool better or equal; VT may oversubscribe carriers.

---

## Reporting Template

```text
Hardware / JDK:
Workload:
Concurrency:
Bottleneck observed:
Result:
Conclusion for production:
```

### Related

[memory.md](./memory.md) · [when-vt-do-not-help.md](./when-vt-do-not-help.md) · [thread-pinning.md](./thread-pinning.md)
