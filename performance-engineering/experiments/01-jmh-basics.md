# Experiment 01 — JMH Basics

## Question

Does a “clever” sum beat a simple loop **under JMH**, and what lies if you skip warmup?

## Workload

Microbenchmark only — not a web app claim.

## Measure (baseline)

Hand-rolled (intentionally flawed):

```java
long t0 = System.nanoTime();
int s = 0;
for (int i = 0; i < 1_000_000; i++) s += i;
long t1 = System.nanoTime();
System.out.println(t1 - t0);
```

Run once cold — note instability.

## Hypothesize

JIT + dead-code elimination dominate; need JMH.

## Experiment

```java
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(1)
public class SumBench {
    @Benchmark
    public int simple(Blackhole bh) {
        int s = 0;
        for (int i = 0; i < 1000; i++) s += i;
        return s;
    }
}
```

```bash
java -jar target/benchmarks.jar SumBench -wi 5 -i 5 -f 1
```

## Analyze

Compare score variance cold vs JMH. Observe how returning/`Blackhole` changes results.

## Conclusion format

“On JDK ___, machine ___, JMH avg time for simple sum over 1000 ints was ___ ns/op (±___). Hand-rolled single-shot is not comparable.”

### Related

[../tools/jmh.md](../tools/jmh.md) · [../benchmarking.md](../benchmarking.md)
