# UnaryOperator\<T>

`Function<T,T>` specialization — same type in and out. `UnaryOperator.identity()`.

## Mental Model

```text
T ──apply──► T
normalize / adjust / next-state of same type
```

## Imperative vs Functional

```java
String sku = raw.strip().toUpperCase();

UnaryOperator<String> normalize = s -> s.strip().toUpperCase();
String sku = normalize.apply(raw);
```

## Production Example

```java
public final class MoneyOps {
    public static UnaryOperator<Money> addTax(Rate rate) {
        return m -> m.plus(m.percent(rate));
    }

    public static UnaryOperator<Money> discount(long offCents) {
        return m -> m.minus(new Money(offCents, m.currency()));
    }
}

Money priced = MoneyOps.addTax(Rate.of("0.10"))
        .andThen(MoneyOps.discount(50))
        .apply(base);
```

`List.replaceAll(UnaryOperator)`, `UnaryOperator.identity()` in maps.

## When Better / Worse

| Better | Worse |
|--------|-------|
| Same-type transforms | When input/output types differ — use `Function` |
| `replaceAll` | Obscure identity hacks |

## Interview / PE

- UnaryOperator vs Function?  
- **PE:** pricing operators as composable UnaryOperator\<Money> — vs pricing service class?

### Related

[function.md](./function.md) · [binary-operator.md](./binary-operator.md) · [function-composition.md](./function-composition.md)
