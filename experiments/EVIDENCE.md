# Experiment evidence index

Local teaching runs on openjdk version "25.0.3" 2026-04-21; Linux 7.0.0-28-generic x86_64; Date: 2026-08-03. Not JMH / not publication benchmarks.

| Lab | One-line key metric |
| --- | --- |
| virtual-vs-platform-blocking | platform pool wallMs=1266; virtual per-task wallMs=35 (tasks=2000 delayMs=10 platformPool=16) |
| allocation-rate-pressure | allocating iters=42213131; reuse-buffer iters=30230967; GC through GC(43) young pauses ~0.685/0.371/0.361ms |
| jit-warmup-cold-vs-hot | early median=1.226ms → after warmup median=0.787ms (50000 crunch() per sample) |
| atomic-vs-synchronized-counter | AtomicInteger ops/s≈50601390; synchronized ops/s≈9507277 (threads=8 seconds=2) |
| platform-thread-footprint | n=2000; platform startWallMs=96; virtual startWallMs=48 (both 2000/2000) |
| nanotime-measurement-pitfalls | single-shot 1263–320 ns; warmed batch median=213832 ns (min=36777 max=410172) |
| hashmap-resize-cost | default HashMap median=36.26ms; pre-sized HashMap(n) median=30.40ms (n=1000000) |
| hashmap-collision-treeify | pathological get median=1086.45ms; normal Integer get median=1.29ms (n=20000) |
| arraylist-amortized-growth | default ArrayList median=42.64ms; ArrayList(n) median=18.49ms |
| concurrent-map-scalability | ConcurrentHashMap ops/s≈62467137; synchronized HashMap ops/s≈8255314 (threads=8 seconds=2) |
| escape-or-not-demo | non-escaping batches=15269244; escaping-list batches=1571547; ratio ≈9.72 (1500ms window) |
| lock-contention-modes | synchronized≈11369364; ReentrantLock≈14995969; LongAdder≈255564709 ops/s (threads=8 seconds=2) |
| completablefuture-fanout | sequential wallMs=360; async fan-out wallMs=136 |
| stream-lazy-shortcircuit | found=1000 peeks=1001 n=1000000 |
| parallel-stream-when-slower | tiny: seq 0.01ms / par 0.17ms; large cheap: seq 5.17ms / par 0.88ms; heavier: seq 0.61ms / par 0.32ms |
| io-buffered-vs-unbuffered | unbuffered median=2586.23ms; BufferedOutputStream median=10.65ms (2M single-byte writes) |
