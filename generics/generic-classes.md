# Generic Classes

A class parameterized by one or more type variables — one implementation, many safe instantiations.

## Mental Model

```text
class Cache<K, V>  =  template over key/value types
Cache<String, User> and Cache<Sku, Product> share bytecode shape; differ at compile-time use
```

## Simple Example

```java
public final class Box<T> {
    private T value;
    public void set(T value) { this.value = value; }
    public T get() { return value; }
}

Box<Money> box = new Box<>();
box.set(new Money(100, "USD"));
Money m = box.get();
```

## Advanced Example (realistic library)

```java
public final class LoadingCache<K, V> {
    private final ConcurrentHashMap<K, V> map = new ConcurrentHashMap<>();
    private final Function<? super K, ? extends V> loader;

    public LoadingCache(Function<? super K, ? extends V> loader) {
        this.loader = loader;
    }

    public V get(K key) {
        return map.computeIfAbsent(key, loader::apply);
    }
}

var users = new LoadingCache<String, User>(id -> userPort.load(id));
User u = users.get("c-42");
```

Note PECS on the `Function` — see [pecs.md](./pecs.md).

## Internal Behavior

Erased to raw class; fields of type `T` become `Object` (or bound). Call sites cast. Diamond `<>` infers from target type.

## Production Use Case

`KafkaTemplate`-style clients, repositories, `ResponseEntity<T>`, JSON `TypeReference` wrappers, domain `Result<T>` / `ApiResponse<T>`.

## Common Mistake

```java
class Bad<T> {
    T[] arr = new T[10]; // cannot create generic array directly
}
```

Store `List<T>` or pass `IntFunction<T[]>` / `Class<T>` if you must.

## Interview Trap

“Does `new Box<String>()` create a different runtime class than `new Box<Integer>()`?” — **Same** `Box` class.

## Principal-Level Discussion

Generic classes define **invariant** family types: `Box<String>` is not a subtype of `Box<Object>`. Expose wildcards on *methods* that need flexibility; keep fields as exact `T` when you read and write.

### Related

[generic-methods.md](./generic-methods.md) · [generic-interfaces.md](./generic-interfaces.md) · [limitations.md](./limitations.md)
