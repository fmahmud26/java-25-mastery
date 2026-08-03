# Optional

## Purpose

Make **absence** explicit for return values — not a silver bullet for all nulls.

## Before

```java
User find(String id) {
    return db.get(id); // may be null — callers NPE
}
```

## After

```java
Optional<User> find(UserId id) {
    return Optional.ofNullable(db.get(id));
}

return find(id).map(User::email).orElse("unknown");
```

## Rules (senior)

| Do | Don’t |
|----|-------|
| Return `Optional` from finders | Use as field / DTO / param casually |
| `orElseThrow` for required | `.get()` without check |
| Keep streams flat | `Optional` nested hell |

## Trade-offs

Clarity of absence vs allocation/noise. For APIs with many missing fields, dedicated types/`null` with `@Nullable` may be clearer than Optional everywhere.

## PE Decision

Optional at **service boundaries for “maybe one”**; not inside every private method.

### Related

[null-handling.md](./null-handling.md) · [api-design.md](./api-design.md)
