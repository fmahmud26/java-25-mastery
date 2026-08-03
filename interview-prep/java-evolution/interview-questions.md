# Java Evolution — Interview Questions (L1–L4)

Show eras + judgment, not a memorized JEP list.

---

## Level 1 — Junior

### Name major features introduced in Java 8.

**Answer:** Lambdas, method references, Stream API, `Optional`, `java.time`, default/static methods on interfaces. It shifted Java toward a more functional style while remaining OO.

### What is a record?

**Answer:** A concise immutable data carrier: final class with components, generated accessors/`equals`/`hashCode`/`toString`. Use for DTOs/value data, not for complex mutable behavior.

### What LTS versions matter after 8?

**Answer:** Common enterprise ladder: **11**, **17**, **21**, **25**. Teams usually jump LTS to LTS rather than every six-month release.

---

## Level 2 — Mid-level

### How did pattern matching evolve through recent JDKs?

**Answer:** `instanceof` patterns → switch patterns → record patterns / nested patterns. Goal: safer destructuring and exhaustiveness (especially with sealed types), less casting boilerplate.

### Virtual threads vs the old thread-pool model?

**Answer:** Platform pools limit concurrency because each thread is expensive. Virtual threads are cheap and unmount on blocking, enabling thread-per-request at high scale. Still use platform pools for CPU-bound work; don’t pool virtual threads.

### What problem did JPMS solve, and why do apps still struggle?

**Answer:** Strong encapsulation and reliable configuration of the JDK/platform. Pain: illegal reflective access, split packages, libraries assuming deep reflection — migrations need `--add-opens` triage or dependency upgrades.

---

## Level 3 — Senior

### How would you justify upgrading from 17 to 25 in a business case?

**Answer:** Security patches cadence, Loom/virtual threads maturity, language productivity (patterns/records already partly on 17 — emphasize VT, API/JVM improvements, vendor support window), dependency ecosystem moving forward, and measurable latency/ops wins from pilots — not “new syntax for fun.”

### When would you *not* use records?

**Answer:** JPA entities needing no-arg constructors/mutable state, types with rich invariant logic better as classes, or identity-based mutable domain aggregates. Records excel as values/DTOs/events.

### Preview features in production?

**Answer:** Avoid on critical paths unless policy allows and you accept binary/API churn. Isolate behind modules/flags; track JEPs across releases; prefer waiting for final on LTS.

---

## Level 4 — Expert

### Production on 11: thread-pool exhaustion under load. Leadership wants “rewrite in reactive” or “jump to 25 + virtual threads.” How do you decide?

**Answer (structured):**

1. **Confirm** — pool size, queue time, socket/thread dumps, DB pool vs HTTP pool mismatch.  
2. **Hypotheses** — blocking I/O held platform threads; connection pool smaller than servlet threads; lock contention.  
3. **Options**  
   - Short term: size pools correctly, timeouts, bulkheads, cache hot reads  
   - Medium: JDK 21/25 + virtual threads for blocking style (measure pinning)  
   - Reactive rewrite: only if ecosystem already reactive or extreme streaming needs  
4. **Evidence plan** — canary on VT executor; JFR pinning; p99/error budget; dep compatibility matrix.  
5. **Risk** — agents/profilers, native libs, synchronized pinning, serialization/framework versions.  
6. **Decision** — prefer VT migration if codebase is blocking-synchronous; reactive if already partial or CPU/stream oriented.

**Common Mistake at L4:** Choosing a rewrite for resume-driven reasons without profiling the exhaustion root cause.

---

## Extra ladder prompts (practice)

| L | Prompt |
|---|--------|
| 1 | What is `var` allowed on? |
| 2 | Text blocks vs string concatenation? |
| 3 | Sealed classes vs enums? |
| 4 | Post-upgrade latency regression — diagnosis path |
