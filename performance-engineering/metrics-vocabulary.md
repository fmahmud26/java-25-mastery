# Metrics Vocabulary

Speak precisely — interviewers and incident channels punish vague words.

## Throughput

Useful work per unit time (requests/s, messages/s, bytes/s). See [throughput.md](./throughput.md).

## Latency

Time for one unit of work to complete. Always state which percentile or histogram. See [latency.md](./latency.md).

## Tail latency

High percentiles (p99, p99.9, max) — where users and SLOs usually hurt. See [tail-latency.md](./tail-latency.md).

## GC pressure

How hard the collector works: allocation rate, frequency, pause distribution, concurrent CPU, after-GC occupancy. See [gc-pressure.md](./gc-pressure.md).

## Contention

Threads blocked on monitors/locks/queues — CPU may look idle while latency soars. See [contention.md](./contention.md).

## Profiling vs benchmarking

| | Profiling | Benchmarking |
|--|-----------|--------------|
| Question | Where is time/memory spent? | How does A compare to B under workload W? |
| Tools | JFR, JMC, jstack, dumps, async-profiler | JMH (micro), Gatling/k6 (macro) |

## Claim template (required)

```text
Under <workload> on <env>, metric <M> improved from <A> to <B>
measured with <tool/method>, n=<runs>, warm/cold=<...>.
Secondary metrics: <CPU, error rate, GC pauses>.
```

### Related

[scientific-method.md](./scientific-method.md) · [profiling.md](./profiling.md)
