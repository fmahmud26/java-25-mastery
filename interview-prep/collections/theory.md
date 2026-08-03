# Collections — Theory

## Contracts

| Type | Duplicate keys/elems | Order | Nulls (typical impl) |
|------|----------------------|-------|----------------------|
| `List` | yes | positional | depends |
| `Set` | no | optional | `HashSet` allows one null |
| `Map` | unique keys | optional | `HashMap` one null key |
| `Queue`/`Deque` | yes | FIFO / ends | impl-specific |

## Mental model

- **Interface** = contract (`Map`).
- **Implementation** = algorithm + memory layout (`HashMap`, `TreeMap`, `LinkedHashMap`).
- Choose by access pattern: get-by-key, range, insertion order, concurrency.

## Invariants interviewers expect

- `equals`/`hashCode` consistency for hash-based structures.
- Fail-fast iterators on structural modification (non-concurrent).
- Complexity claims are *amortized average* unless you say worst-case.

Related chapter: [collection.md](../../collections/collection.md), [map.md](../../collections/map.md).
