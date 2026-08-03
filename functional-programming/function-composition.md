# Function Composition

Build pipelines from small functions — `andThen` / `compose`, plus Predicate/Consumer chaining.

## Mental Model

```text
data → f → g → h → result
prefer andThen for left-to-right reading
```

## Imperative vs Functional

```java
String sku = raw.strip();
sku = sku.toUpperCase();
boolean ok = catalog.exists(sku);

Function<String, String> norm = ((Function<String, String>) String::strip).andThen(String::toUpperCase);
boolean ok = catalog.exists(norm.apply(raw));
```

## Production Example

```java
Function<Cart, Cart> attachTax = taxService::apply;
Function<Cart, Cart> attachShipping = shippingService::apply;
Function<Cart, Quote> toQuote = Quote::fromCart;

Function<Cart, Quote> checkoutPipeline =
        attachTax.andThen(attachShipping).andThen(toQuote);

Quote quote = checkoutPipeline.apply(cart);
```

When steps have I/O and failure modes, a named `CheckoutService` method is clearer than a composed `Function` — composition of **pure** steps shines; impure pipelines need explicit orchestration.

## andThen vs compose

| Call | Meaning |
|------|---------|
| `f.andThen(g)` | `g(f(x))` — f then g |
| `f.compose(g)` | `f(g(x))` — g then f |

Details: [and-then.md](./and-then.md) · [compose.md](./compose.md)

## When Better / Worse

| Better | Worse |
|--------|-------|
| Pure normalization / mapping chains | Transactional multi-step checkout only as Function |
| Shared partial pipelines | Deep compose that’s hard to breakpoint |

## Performance & Readability

Composition is cheap (extra apply calls; JIT may inline). Readability fails first — name the pipeline.

## Interview / PE

- Draw `andThen` vs `compose`  
- **PE:** when promote a composed Function to a domain service class?

### Related

[function.md](./function.md) · [higher-order-functions.md](./higher-order-functions.md) · [when-to-use.md](./when-to-use.md)
