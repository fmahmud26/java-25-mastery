# Collections — Internals

Focus: **HashMap** (the usual deep-dive).

```text
key → spread(hash) → index = hash & (n-1)
                   → bin: list → (if long) red-black tree
```

## Must-explain pieces

| Piece | Point |
|-------|-------|
| Table | Power-of-two array of bins |
| Load factor | Default 0.75; resize when size > capacity×load |
| Resize | New table, recompute indices (often bit trick) |
| Treeify | Long colliding bins become trees (worst-case lookup) |
| `hashCode`/`equals` | Bucket then equality |

`ConcurrentHashMap`: bin/cas granularity — **not** a synchronized `HashMap`. No null keys/values.

`ArrayList`: contiguous `Object[]`, amortized O(1) add at end, O(n) mid insert.

Whiteboard until you can draw put/get/resize without notes.

Related: [hashing.md](../../collections/hashing.md), [hash-collisions.md](../../collections/hash-collisions.md), [rehashing.md](../../collections/rehashing.md).
