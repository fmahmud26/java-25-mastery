# Lazy Evaluation

Intermediate operations record *what* to do; **nothing runs** until a terminal operation pulls data.

## What Happens

```java
var pipeline = transactions.stream()
        .filter(Tx::settled)
        .map(Tx::amountCents);  // no iteration yet

long sum = pipeline.mapToLong(Long::longValue).sum(); // now runs
```

With short-circuit:

```text
findFirst: process until first match — skip the rest
limit(n):  at most n elements through upstream
```

## Why Useful

Avoids wasted work; enables infinite sources with `limit`; fuses stages element-by-element (no full intermediate lists unless stateful ops force buffering).

## Production Example — logs

```java
Optional<String> firstError = Files.lines(logPath)
        .filter(l -> l.contains("ERROR"))
        .filter(l -> l.contains("payment-service"))
        .findFirst(); // stops reading file early (with buffered read semantics)
```

## Performance Implications

Laziness ≠ free. Stateful ops (`sorted`, `distinct`, `limit` on ordered parallel) may buffer. Side effects in intermediates run at unpredictable times/order under parallel.

## Common Mistake

Assuming `map` ran because it was “called” in source — without a terminal, it didn’t. Putting metrics inside `map` and expecting them before terminal returns in a specific order.

### Related

[stream-lifecycle.md](./stream-lifecycle.md) · [intermediate-operations.md](./intermediate-operations.md) · [side-effects.md](./side-effects.md)
