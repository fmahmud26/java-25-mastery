# Lambda Expressions

Concise SAM implementations — the syntax of Java’s functional style.

## Mental Model

```text
(parameters) -> body
type comes from target (assignment / param / cast)
```

## Imperative vs Functional

```java
// Imperative sort
Collections.sort(orders, new Comparator<Order>() {
    public int compare(Order a, Order b) {
        return Long.compare(a.totalCents(), b.totalCents());
    }
});

// Lambda
orders.sort((a, b) -> Long.compare(a.totalCents(), b.totalCents()));

// Method reference style
orders.sort(Comparator.comparingLong(Order::totalCents));
```

## Production Example

```java
@RestController
public class RefundController {
    private final RefundService refunds;

    @PostMapping("/refunds")
    ResponseEntity<?> create(@RequestBody RefundRequest req) {
        return refunds.refund(req)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}

// worker scheduling
ExecutorService pool = Executors.newVirtualThreadPerTaskExecutor();
pool.execute(() -> gateway.capture(cmd));
```

Keep lambdas short — extract `private` methods when business rules grow.

## Internal Behavior

Target typing; capture of effectively-final locals; `invokedynamic`. Block bodies need `return` for non-void SAMs. Checked exceptions: must catch inside or use sneaky/wrapping helpers — SAMs rarely declare checked exceptions.

## When Better / Worse

| Better | Worse |
|--------|-------|
| Comparators, callbacks, stream ops | 30-line lambdas with nested try/catch |
| Replacing anonymous SAMs | Need fields/multiple methods → class |

## Performance & Readability

Non-capturing lambdas are cheap. Capturing + streams in hot paths — measure. Readability: prefer method refs when they name the action.

## Common Mistake

Mutating an outer local via array/Atomic “escape hatch” for shared counters inside parallel streams — races/bugs. Prefer proper reducers.

## Interview / PE

- Effectively final why?  
- Lambda vs anonymous class?  
- **PE:** coding standard for max lambda size in payments service?

### Related

[method-references.md](./method-references.md) · [effectively-final.md](./effectively-final.md) · [closures.md](./closures.md)
