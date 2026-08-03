# BiFunction\<T,U,R>

`R apply(T t, U u)` — two inputs, one output. Core to `Map.compute` / merges.

## Mental Model

```text
(T, U) ──apply──► R
```

## Imperative vs Functional

```java
Integer cur = stock.get(sku);
stock.put(sku, cur == null ? delta : cur + delta);

stock.merge(sku, delta, Integer::sum);
stock.compute(sku, (k, v) -> v == null ? delta : v + delta);
```

## Production Example

```java
BiFunction<FxRate, Money, Money> toUsd = (rate, money) -> money.convert(rate);

BiFunction<Cart, PricingContext, Quote> price =
        (cart, ctx) -> pricingEngine.quote(cart, ctx);

Quote q = price.apply(cart, ctx);
```

`BinaryOperator` is the same-type special case.

## When Better / Worse

Natural for Map remapping and pair transforms. Prefer a named domain method when the lambda grows.

## Interview / PE

- BiFunction vs BinaryOperator?  
- **PE:** CHM `compute` — atomicity per key, not across keys?

### Related

[binary-operator.md](./binary-operator.md) · [bi-consumer.md](./bi-consumer.md) · [function.md](./function.md)
