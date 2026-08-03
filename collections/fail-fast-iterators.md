# Fail-Fast Iterators

Most `java.util` collections track `modCount`; iterators expect structural stability.

## How It Works

Iterator snapshots expected modCount; structural change → `ConcurrentModificationException` (best-effort, not a guarantee against all races).

## Not Fail-Fast

Concurrent collections often **weakly consistent** iterators (e.g. CHM, COW snapshot).

## Failure Scenario

```java
for (String id : map.keySet()) {
    map.remove(id); // CME risk — use Iterator.remove or removeIf
}
map.entrySet().removeIf(e -> stale(e));
```

## Interview

- What does fail-fast mean?  
- Is CME a reliable concurrency detector? (**No**)  
- CHM iterator behavior vs HashMap?

### Related

[hashmap.md](./hashmap.md) · [concurrenthashmap.md](./concurrenthashmap.md) · [copyonwritearraylist.md](./copyonwritearraylist.md)
