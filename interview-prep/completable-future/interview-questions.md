# CompletableFuture — Interview Questions (L1–L4)

Composition + executor choice + VT interaction.

---

## Level 1 — Junior

### What is a CompletableFuture?

**Answer:** A `Future` you can complete explicitly and **compose** with functional stages (`thenApply`, `thenCompose`, …). It models asynchronous pipelines, not just “run and get.”

### `thenApply` vs `thenAccept`?

**Answer:** `thenApply` transforms a value → new value. `thenAccept` consumes the value (side effect) → `Void` stage.

### How do you start async work?

**Answer:** `CompletableFuture.supplyAsync(supplier)` or `runAsync(runnable)`, optionally with an `Executor`.

---

## Level 2 — Mid-level

### `thenCompose` vs `thenApply` when the function returns a future?

**Answer:** `thenApply` would yield `CompletableFuture<CompletableFuture<T>>`. `thenCompose` flattens to `CompletableFuture<T>` — use it for dependent async calls.

### Why is using the common pool for blocking I/O dangerous?

**Answer:** `ForkJoinPool.commonPool()` is shared and sized for CPU parallelism. Blocking tasks hog workers → stalled unrelated parallel work and CF stages.

### How do timeouts work?

**Answer:** `orTimeout` completes exceptionally after a delay; `completeOnTimeout` completes with a fallback value. Always pair remote calls with timeouts.

---

## Level 3 — Senior

### How do virtual threads change CompletableFuture usage on Java 25?

**Answer:** Blocking style on VTs often replaces deep CF graphs for I/O fan-out. When you still use CF, pass `newVirtualThreadPerTaskExecutor()` for blocking stages instead of commonPool. Blocking `join`/`get` on a VT is much cheaper than on a platform thread, but structure and timeouts still matter. Consider structured concurrency for cancel/fail-fast scopes.

### `allOf` result handling?

**Answer:** `allOf` gives `CompletableFuture<Void>`. Join it, then `join()` each input future (already complete) to collect values; handle partial failures explicitly.

### Exception propagation gotchas?

**Answer:** Unchecked wrap in `CompletionException` on `join`. Stages after a failure skip unless you `handle`/`exceptionally`. `whenComplete` observes but doesn’t recover unless you chain further.

---

## Level 4 — Expert

### Production: API gateway uses CF fan-out; after traffic growth, FJP active threads pegged, p99 explodes, some requests hang until tomcat threads exhaust. Diagnose?

**Answer (structured):**

1. **Confirm** — dumps show threads blocked in CF stages / HTTP clients; commonPool saturation; Tomcat waiting on `get`.  
2. **Hypotheses** — blocking I/O on commonPool; no timeouts; unbounded fan-out; platform request thread blocked on `join`.  
3. **Evidence** — JFR/executor metrics; dependency latency; stage counts.  
4. **Remedies**  
   - Move blocking stages to VT executor or dedicated bounded pool  
   - Timeouts + bulkheads per dependency  
   - Avoid blocking platform request threads — reactive return or VT request model  
   - Limit in-flight `allOf` size  
5. **Longer term** — simplify to VT structured fan-out if graph is I/O only.  
6. **Validate** — load test, pool metrics, p99, no commonPool blocking.

**Common Mistake at L4:** “Add more FJP parallelism” while keeping blocking I/O on the common pool.

---

## Extra ladder prompts (practice)

| L | Prompt |
|---|--------|
| 1 | `join` vs `get`? |
| 2 | `thenCombine` use case? |
| 3 | Propagating MDC across stages |
| 4 | Retry storm via exceptional recovery — diagnosis |
