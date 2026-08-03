# andThen

Chain **after** the current function/consumer.

```text
f.andThen(g).apply(x) ≡ g(f(x))
```

## Example

```java
Function<String, String> strip = String::strip;
Function<String, Integer> len = String::length;
int n = strip.andThen(len).apply("  pay  "); // 3

Consumer<Payment> post = ledger::post;
Consumer<Payment> audit = auditLog::write;
payments.forEach(post.andThen(audit));
```

## When to Prefer

Left-to-right data flow matches how people read pipelines — usually prefer `andThen` over `compose`.

## Interview

What is `f.andThen(g)` vs `f.compose(g)`?

### Related

[compose.md](./compose.md) · [function-composition.md](./function-composition.md)
