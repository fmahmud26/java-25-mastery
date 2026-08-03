# Thread Pinning

Pinned = virtual thread **cannot unmount**, so its **carrier stays occupied** during blocking.

## Mental Model

```text
Normal block: VT unmounts → carrier free
Pinned block: VT holds carrier → that OS thread stuck with it
```

Mass pinning → scalability cliff (few carriers blocked ⇒ other VTs wait).

## Java 25 Reality (important)

**JEP 491** (Java 24): *Synchronize Virtual Threads without Pinning* — `synchronized` **no longer pins** virtual threads in HotSpot the way early Loom builds did.

Still true on Java 25 (residual / design):

| Still risky | Why |
|-------------|-----|
| Holding locks across long I/O | Correctness/latency (lock held), even when not pinning |
| JNI / native blocking / FFM downcalls | May keep carrier busy |
| Local file I/O (esp. Linux) | Can still pin — do **not** equate with socket park |
| Class loading / `<clinit>` | Can constrain unmount while initializing |
| CPU-bound sections | Carrier occupied by design |
| Older JDKs (21) | `synchronized` pinning still a major teaching point |

## Code — design hygiene (all JDKs)

```java
// Bad style — lock held across remote call
synchronized (lock) {
    return http.send(req, BodyHandlers.ofString()).body();
}

// Better
String cached;
synchronized (lock) {
    cached = map.get(key);
}
if (cached != null) return cached;
String body = http.send(req, BodyHandlers.ofString()).body();
synchronized (lock) {
    map.put(key, body);
}
return body;
```

## Detecting Pinning

- **Primary (Java 25):** JFR `jdk.VirtualThreadPinned` (and related VT events).  
- `jdk.tracePinnedThreads` was removed on modern JDKs (24+) — do not rely on it for Java 25.  
- Symptom: low app progress, carriers busy, many VTs runnable waiting.

## Production Scenario

Legacy library does I/O inside `synchronized` on JDK 21 → pinning storm after VT migration. On JDK 25, pinning from synchronized largely gone — still refactor I/O out of locks.

## Failure Scenario

“We migrated to VT and throughput dropped” on JDK 21 due to pinning; or on any JDK due to lock-over-I/O / native.

## Interview Questions

- What is pinning?  
- What changed with JEP 491 / Java 24+?  
- Why still avoid I/O under locks?

## Principal-Level Discussion

Pinning literacy includes **version**. PE answer: cite JEP 491 for 25, still ban lock-over-I/O, still watch JNI, measure with JFR during migration.

### Related

[synchronization-and-vt.md](./synchronization-and-vt.md) · [carrier-threads.md](./carrier-threads.md) · [blocking-io.md](./blocking-io.md)
