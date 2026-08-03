# Switch Expressions

**Introduced:** preview 12–13 · **Final:** Java 14 · **Java 25:** standard; pairs with pattern switch (21+)

## Problem Before

Classic `switch` was a statement, fell through by default, and didn’t produce a value cleanly:

```java
String label;
switch (status) {
    case "PAID":
        label = "ok";
        break;
    case "FAILED":
        label = "bad";
        break;
    default:
        label = "unknown";
}
```

Missed `break` → subtle bugs.

## The Feature

`switch` as an **expression**: yields a value, arrow labels don’t fall through, exhaustiveness required for enums/sealed (with patterns).

## How It Works

- `case A, B -> value`  
- Block form uses `yield`  
- Colon form still exists for statements; prefer arrows for new code  
- Combined with type/record patterns on Java 21+

## Before → After

```java
// After
String label = switch (status) {
    case PAID -> "ok";
    case FAILED, CANCELLED -> "bad";
    case PENDING -> "wait";
};

int feeCents = switch (region) {
    case "EU" -> 20;
    case "US" -> 30;
    default -> {
        audit.unknownRegion(region);
        yield 50;
    }
};
```

## Production Usage

- Map enums/statuses to handlers, fees, HTTP codes  
- Prefer over `if-else` chains when switching on one discriminant  
- With sealed events: exhaustive routing (see pattern switch)

## Trade-offs

| Pros | Cons |
|------|------|
| Safer, expression-friendly | Large switches can still be “god routers” |
| Exhaustiveness | Need `default` for open types (`String`, unbound enums evolving carefully) |

## When NOT to Use

- Polymorphic behavior better via interface methods (OCP with adapters)  
- Open-ended stringly protocols without defaults/monitoring  
- Side-effect-only flows where a statement + early return is clearer

## Migration Notes

Convert value-producing switches first. Leave giant statement switches alone until you add sealed types. Enable lint for missing enum cases.

## Interview Questions

- Expression vs statement switch?  
- Why no fall-through with `->`?  
- When is `yield` required?  
- How does exhaustiveness interact with sealed types?

### Related

[pattern-matching-for-switch.md](./pattern-matching-for-switch.md) · [sealed-classes.md](./sealed-classes.md)
