# Throughput

Work completed per unit time under a defined load and success criteria.

## Mental Model

```text
throughput = successful operations / time
(state error rate — failed work is not throughput)
```

## How to Measure

| Layer | Tools |
|-------|-------|
| Service | Load generator (k6, Gatling, wrk) RPS + success |
| Micro | JMH `Mode.Throughput` / `ops/s` |
| Pipeline | Messages processed/s, bytes/s |

Always pair with latency — max RPS at unacceptable p99 is not a win.

## Hypotheses that affect throughput

- CPU-bound hot methods  
- GC stealing cores  
- Lock/queue serialization  
- Downstream rate limits  
- Insufficient concurrency (or too much → thrash)

## Experiment sketch

Hold payload and machine fixed; ramp concurrency; plot RPS vs p99. Find the knee where tails explode.

## Claim template

“Under mixed-read workload W on 4 vCPU JDK25/G1, sustained successful RPS rose from 4.2k to 5.1k after removing lock X; p99 stayed ≤ 45ms (n=3×10min).”

### Related

[latency.md](./latency.md) · [contention.md](./contention.md) · [experiments/README.md](./experiments/README.md)
