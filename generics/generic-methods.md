# Generic Methods

Methods that introduce their **own** type parameters — independent of (or in addition to) the enclosing type.

## Mental Model

```text
static <T> List<T> of(T... values)   →  T inferred per call
class Util { <T> T pick(T a, T b) }  →  instance generic method
```

Prefer a generic method when only one operation needs `T`, not the whole class.

## Simple Example

```java
public static <T> T requireNonNull(T value, String message) {
    if (value == null) throw new IllegalArgumentException(message);
    return value;
}

String id = requireNonNull(paymentId, "paymentId");
```

## Advanced Example (API helper)

```java
public final class Json {
    private final ObjectMapper mapper = new ObjectMapper();

    public <T> T read(String body, Class<T> type) throws IOException {
        return mapper.readValue(body, type); // Class<T> reifies T at the edge
    }

    public <T> T read(String body, TypeReference<T> type) throws IOException {
        return mapper.readValue(body, type); // nested generics: List<Order>
    }
}

List<Order> orders = json.read(payload, new TypeReference<>() {});
```

```java
public static <T> void copy(List<? extends T> src, List<? super T> dest) {
    for (T t : src) dest.add(t);
}
```

## Internal Behavior

Inference solves `T` from arguments and target type. Bridges may appear when overriding. Erasure still applies — `Class<T>` / `TypeReference` are explicit runtime tokens.

## Production Use Case

`Collections.sort`, `Optional.map`, Spring `RestTemplate.exchange(..., ParameterizedTypeReference)`, mapping layers `toDto(Entity e)`.

## Common Mistake

Declaring a useless class-level `T` when only one static helper needs it — pollutes the type.

## Interview Trap

```java
static <T> T bad() { return null; } // legal but useless
static <T> List<T> oops() { return new ArrayList<String>(); } // error
```

Inference/`T` identity must be consistent; you can’t pretend `String` is arbitrary `T` without unchecked cast.

## Principal-Level Discussion

Generic methods + PECS are how JDK APIs stay flexible (`Comparator.comparing`, stream collectors). At boundaries that need nested types, require a **type token** (`Class`/`TypeReference`) — don’t fake reification.

### Related

[generic-classes.md](./generic-classes.md) · [pecs.md](./pecs.md) · [type-erasure.md](./type-erasure.md)
