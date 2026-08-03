# Null Handling

## Before

```java
String email = user.getEmail().toLowerCase(); // NPE
```

## After

```java
Optional.ofNullable(user.email())
        .map(String::toLowerCase)
        .orElse("unknown");
// or require non-null in record compact ctor
```

Prefer **designing null out** (required fields in records) over sprinkling `?.`-style checks everywhere.

### Related

[optional.md](./optional.md) · [defensive-programming.md](./defensive-programming.md)
