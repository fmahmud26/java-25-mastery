# Set

Collection of **unique** elements — uniqueness via `equals`/`hashCode` or comparator.

## 1. Mental Model

```text
add(A), add(B), add(A) → {A, B}
```

## 2. Contract

- At most one of each equal element.  
- `null` policy depends on impl (HashSet allows one null; TreeSet needs comparable null policy — usually avoid null).  
- No index access.

## 3. Implementations

| Impl | Order | Structure |
|------|-------|-----------|
| [HashSet](./hashset.md) | Unspecified | HashMap keys |
| [LinkedHashSet](./linkedhashset.md) | Insertion | LinkedHashMap |
| [TreeSet](./treeset.md) | Sorted | TreeMap |
| Concurrent | — | `ConcurrentHashMap.newKeySet()` |

## 4. Scenarios

- **Session tokens seen:** HashSet (bounded + eviction!).  
- **Tags on product:** LinkedHashSet if display order matters.  
- **Sorted SKUs:** TreeSet / DB index.

## 5. Decision

Need speed → HashSet. Need order → LinkedHashSet. Need range/sorted → TreeSet. Concurrent → CHM key set.

## 6. Failure Scenario

Mutable element changed after insert → “lost” in HashSet (bucket wrong). **Immutable elements only.**

## 7. Interview

- How does HashSet guarantee uniqueness?  
- TreeSet vs HashSet complexity?  
- **Principal:** rate-limit “seen keys” with a Set — what bounds/eviction?

### Related

[hashset.md](./hashset.md) · [map.md](./map.md) · [equals.md](./equals.md) · [hashcode.md](./hashcode.md)
