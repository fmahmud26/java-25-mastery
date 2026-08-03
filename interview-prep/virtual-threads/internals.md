# Virtual Threads — Internals

Focus: **carriers, mount/unmount, pinning, scheduler**.

```text
VT runnable → mount on carrier → run Java frames
VT blocks (JDK-aware I/O / park) → unmount → carrier free for other VTs
VT pinned → cannot unmount → carrier blocked with the VT
```

## Must-explain pieces

| Piece | Point |
|-------|-------|
| Carrier | Platform thread that currently executes a VT |
| Mount / unmount | VT ↔ carrier binding; unmount on blocking frees carrier |
| Scheduler | Default virtual-thread scheduler (FJP-style work-stealing) |
| Continuation | VT stack/continuation captured when unmounted (conceptual) |
| Pinning | Blocking while JVM can’t unmount — carrier occupied |
| Thread locals | Supported; heavy `ThreadLocal` caches can bloat with millions of VTs |

## Pinning (Java 21 → 25)

| Cause | Notes |
|-------|-------|
| `synchronized` + block inside | **JDK 21:** pinned. **Java 24+ / JEP 491:** no longer pins that way — still avoid long I/O under locks |
| Native / JNI / FFM | Carrier may stay blocked |
| Local file I/O (Linux) | Residual pin risk — measure |
| Class loading / `<clinit>` | Can constrain unmount |

```java
// Still best practice: don’t block while holding the monitor
String cached;
synchronized (lock) {
    cached = map.get(key);
}
if (cached == null) {
    cached = remoteFetch();      // unmount-friendly on VT
    synchronized (lock) {
        map.put(key, cached);
    }
}
```

## Observability

- Thread dumps show virtual threads and carrier relationships.
- **Java 25:** JFR `jdk.VirtualThreadPinned` (and related VT events).
- `jdk.tracePinnedThreads` is **not** a Java 25 troubleshooting path (removed on modern JDKs).

Related: [jvm-scheduling.md](../../virtual-threads/jvm-scheduling.md), [thread-pinning.md](../../virtual-threads/thread-pinning.md), [blocking-io.md](../../virtual-threads/blocking-io.md).
