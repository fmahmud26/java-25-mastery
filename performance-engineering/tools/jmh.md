# Tool: JMH

**Java Microbenchmark Harness** (OpenJDK) — correct microbenchmarks for JDK code.

## Setup

Use official JMH archetype/docs for your build tool. Core idea: annotate `@Benchmark`, let JMH warmup/fork/measure.

```bash
java -jar target/benchmarks.jar -wi 5 -i 5 -f 2
```

## Essentials

| Feature | Why |
|---------|-----|
| Warmup | JIT tiers stabilize |
| Forks | Separate JVM noise |
| Blackhole / return value | Defeat DCE |
| Modes | Throughput, AverageTime, SampleTime |
| Profilers | Optional JMH profilers — still verify |

## Never

Hand-roll timers as proof for tiny methods. Extrapolate JMH ns/op to cluster p99 without macro tests.

### Related

[../benchmarking.md](../benchmarking.md) · [../experiments/01-jmh-basics.md](../experiments/01-jmh-basics.md)
