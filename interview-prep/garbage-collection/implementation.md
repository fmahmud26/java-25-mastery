# Garbage Collection — Implementation

Flags and signals you’ll actually use (Java 25 / HotSpot).

```bash
# G1 with pause goal
java -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -Xms2g -Xmx2g -jar app.jar

# ZGC on Java 25 — generational is the only mode; do NOT pass -XX:+ZGenerational (obsolete)
java -XX:+UseZGC -Xmx8g -jar app.jar

# Diagnostics
java -Xlog:gc*:file=gc.log:time,uptime,level,tags \
     -XX:+HeapDumpOnOutOfMemoryError -jar app.jar
```

## Selection cheat sheet

| Need | Reach for |
|------|-----------|
| Balanced server default | G1 |
| Strict p99 / large heap | ZGC (generational) |
| Max throughput batch | Parallel (measure) |
| Tiny footprint | Serial |
| See why GC ran | `-Xlog:gc*` / JFR GC events |

## Allocation failure (say it precisely)

When Eden (or a region) cannot satisfy an allocation → young/collect cycle is triggered (or promotion/old collection paths). **Allocation failure** is a normal trigger, not itself a bug. Pathological: failure → collect → still can’t allocate → Full GC / OOME.

```java
// App-level: reduce pressure; don’t “call System.gc()” in hot paths
List<byte[]> leak = new ArrayList<>();
for (int i = 0; i < 1_000_000; i++) leak.add(new byte[1024]); // demo only
```

Related: [g1-gc.md](../../garbage-collection/g1-gc.md), [zgc.md](../../garbage-collection/zgc.md).
