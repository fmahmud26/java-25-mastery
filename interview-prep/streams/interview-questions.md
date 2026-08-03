# Streams — Interview Questions (L1–L4)

Escalate only when the interviewer pushes. Lazy pipeline → collectors → parallel pitfalls → production diagnosis.

---

## Level 1 — Junior

### What is a Stream?

**Answer:** A sequence of elements supporting sequential or parallel aggregate operations. It is **not** a data structure — it doesn’t store elements; it pipelines a source through intermediate ops to a terminal op.

### Intermediate vs terminal operations?

**Answer:** Intermediate (`map`, `filter`) are lazy and return a stream. Terminal (`collect`, `forEach`, `count`) trigger execution and produce a result or side effect. After a terminal op the stream is consumed.

### How do you collect to a List?

**Answer:** `.toList()` (unmodifiable, Java 16+) or `.collect(Collectors.toList())` (modifiable ArrayList historically). Prefer `toList()` when mutation isn’t needed.

---

## Level 2 — Mid-level

### Why are streams lazy?

**Answer:** Intermediate ops build a pipeline without touching the source. Laziness allows fusion (per-element pull through stages) and short-circuiting (`findFirst`, `anyMatch`) so unused elements aren’t processed.

### `map` vs `flatMap`?

**Answer:** `map` is 1→1 element transform. `flatMap` is 1→N: each element produces a stream whose contents are concatenated into the downstream (flatten nested structures).

### How does `collect` work?

**Answer:** A `Collector` supplies a container, accumulates elements into it, optionally combines partial containers (parallel), and finishes. Examples: `groupingBy`, `joining`, `toMap`. Prefer collect for mutable aggregation; `reduce` for algebraic folds.

---

## Level 3 — Senior

### Parallel stream pitfalls?

**Answer:**  
- Shared mutable state → races.  
- Non-associative reductions → wrong results.  
- Ordered ops (`findFirst`, `forEachOrdered`, some `limit`) limit speedup.  
- Uses common ForkJoinPool by default — blocking tasks starve other parallel streams.  
- Overhead on small datasets.  
Prefer `findAny` when order doesn’t matter; prove speedup with benchmarks.

### When should you *not* use streams?

**Answer:** Hot tight loops on primitives where boxing hurts; complex control flow; checked-exception-heavy code; parallel IO; or when a simple for-loop is clearer. Streams aren’t inherently faster.

### Stateful intermediate ops — why care?

**Answer:** `sorted`, `distinct`, `limit`/`skip` may need buffering or global coordination. In parallel they introduce barriers and more memory. Push filters before sort when semantics allow.

---

## Level 4 — Expert

### Production: API p99 spikes after enabling `parallelStream()` for JSON field mapping on each request. CPU threads blocked; other unrelated parallel streams hang. Diagnose.

**Answer (structured):**

1. **Confirm symptoms** — latency regression coincides with deploy; thread dump shows `ForkJoinPool.commonPool` workers blocked in HTTP/DB/jackson; unrelated jobs slow.  
2. **Tools** — thread dumps / JFR; FJP targeted; allocation; compare sequential baseline.  
3. **Hypotheses**  
   - Blocking IO inside parallel stream on **common pool**  
   - Tiny per-request datasets → overhead only  
   - Non-thread-safe shared buffer in lambda  
   - Nested `parallelStream` deadlocks / saturation  
4. **Evidence** — stack traces in socket read inside FJP workers; pool size == cores; request N small.  
5. **Remedies (risk-ordered)**  
   - Revert to sequential stream for request path  
   - Move IO to virtual threads / async client — not parallel streams  
   - If pure CPU & large batch: dedicated FJP via `parallel()` only on batch jobs, never on request thread with blocking  
   - Remove shared mutation  
6. **Validate** — load test p99; common pool idle under traffic; alerts on FJP queued tasks.

**Common Mistake at L4:** “Just increase ForkJoinPool parallelism” while keeping blocking work on the common pool.

---

## Extra ladder prompts (practice)

| L | Prompt |
|---|--------|
| 1 | Is `peek` intermediate or terminal? |
| 2 | `findFirst` vs `findAny`? |
| 3 | Why can `groupingBy` concurrent collector help? |
| 4 | GC thrash from stream pipeline allocating per element — approach? |
