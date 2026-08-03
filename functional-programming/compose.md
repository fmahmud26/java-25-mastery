# compose

Chain **before** the current function.

```text
f.compose(g).apply(x) ≡ f(g(x))
```

## Example

```java
Function<Integer, Integer> square = x -> x * x;
Function<Integer, Integer> inc = x -> x + 1;

square.compose(inc).apply(3); // square(4) = 16
square.andThen(inc).apply(3); // 9 + 1 = 10
```

## Readability Note

`upper.compose(trim)` equals `trim.andThen(upper)` — pick one style in a codebase (prefer `andThen`).

## Interview

When would `compose` read better? (Math-style “f ∘ g”.)

### Related

[and-then.md](./and-then.md) · [function-composition.md](./function-composition.md)
