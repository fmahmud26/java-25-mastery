# Throughput

Work completed per unit time under a defined load and **success** criteria.

## Mental Model

```text
throughput = successful operations / time
(state error rate — failed work is not throughput)
```

Pair every throughput claim with a latency percentile. Max RPS at unacceptable p99 is not a win.

## How to Measure

| Layer | Tools |
|-------|-------|
| Service | Load generator (k6, Gatling, wrk) RPS + success |
| Micro | JMH `Mode.Throughput` / `ops/s` |
| Pipeline | Messages processed/s, bytes/s |

## Production Scenario — knee of the curve

Ramp concurrency 8→512. Success RPS climbs then falls while CPU stays high — thrash / lock / GC. Operate below the knee with headroom; see [high-throughput-systems.md](./high-throughput-systems.md).

## Hypotheses that affect throughput

- CPU-bound hot methods  
- GC stealing cores  
- Lock/queue serialization  
- Downstream rate limits  
- Insufficient concurrency (or too much → thrash)  
- Unbounded VT fan-out starving a small pool  

## Experiment sketch

Hold payload and machine fixed; ramp concurrency; plot success RPS vs p99. Find the knee where tails explode. Change one bottleneck; re-measure.

## When Not to Maximize Throughput

Latency-critical paths; strongly consistent ledgers where batching breaks semantics; dependency quota is the real ceiling.

## Claim template

“Under mixed-read workload W on 4 vCPU JDK25/G1, sustained successful RPS rose from 4.2k to 5.1k after removing lock X; p99 stayed ≤ 45ms (n=3×10min).”

## Principal Perspective

Ask what **successful** means (HTTP + business commit). Design admit/shed before adding threads.

### Related

[latency.md](./latency.md) · [contention.md](./contention.md) · [high-throughput-systems.md](./high-throughput-systems.md) · [experiments/README.md](./experiments/README.md)
