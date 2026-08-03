# Loops

Repeated execution with clear **termination** — for, enhanced-for, while, do-while.

## 1. Mental Model

```text
bounded:   for (i=0; i<n; i++)     // N known
streaming: while (hasNext())       // until source ends
retry:     while (attempts < MAX)  // hard ceiling
```

## 2. Simple Explanation

Loops repeat work until a condition fails. Production loops need a stop policy: size bound, timeout, max attempts — never “spin forever and hope.”

## 3. Technical Explanation

| Form | Use |
|------|-----|
| `for` | Index / counted |
| enhanced-for | Iterate Iterable/array |
| `while` | Unknown count, precondition |
| `do-while` | At least once |
| `break`/`continue` | Early exit / skip — sparingly |

Enhanced-for uses `Iterator`; concurrent structural modification → `ConcurrentModificationException` (fail-fast iterators).

## 4. Internal Behavior

Counted loops often become simple jumps. Enhanced-for desugars to iterator. Busy `while(true)` without park/backoff burns a CPU core.

## 5. Java 25 Example

```java
for (OrderLine line : order.lines()) {
    taxService.apply(line);
}

int attempts = 0;
boolean done = false;
while (attempts < MAX_ATTEMPTS && !done) {
    done = paymentGateway.capture(paymentId);
    attempts++;
    if (!done) {
        // production: prefer scheduler/resilience library over raw sleep in request threads
        Thread.sleep(backoffMs(attempts));
    }
}
```

## 6. Real-World Scenario

**Webhook delivery:** infinite retry loop without jitter hammered a partner API → IP ban. Added max attempts, exponential backoff, DLQ.

## 7. Common Mistake

`while(true)` with no escape; modifying a list while enhanced-for iterating; off-by-one in index loops.

## 8. Failure Scenario

CPU pegged, no progress logs — busy poll. Fix blocking wait or scheduler; add metrics for loop iterations/retries.

## 9. Performance Implications

O(n) is fine until n is huge or work inside is heavy. Prefer streaming/pagination over loading all rows into a loop. Avoid allocating inside tight inner loops when hot.

## 10. Interview Questions

- for vs enhanced-for?  
- What is ConcurrentModificationException?

## 11. Senior-Level Follow-ups

- Retry loop design for a flaky dependency?  
- When to replace a loop with Stream/batch API?

## 12. Principal Engineer Perspective

Every loop is a **resource and liveness** policy. Bound it, observe it, and back off under failure.

### Related

[control-flow.md](./control-flow.md) · [arrays.md](./arrays.md) · [operators.md](./operators.md)
