# Hashing (Collections)

How keys become bucket indices — foundation of HashMap/HashSet/CHM.

## Problem

Need O(1)-ish lookup for arbitrary keys without sorting.

## How It Works

```text
hashCode() → (spread) → index = (tableLength - 1) & hash
```

Power-of-two table lengths make masking cheap. Spread mixes high bits so they affect index.

## Production

Bad/constant `hashCode` → one hot bucket → CPU in equals. Mutable keys → lost entries.

## Decision

Custom keys: immutable; include business id fields; test distribution if extreme scale.

## Failure

Pathological collisions → latency. Fix keys; treeify helps HashMap but doesn’t fix equal storms.

## Interview

- Why spread bits?  
- Why power-of-two capacity?  
- Contract of hashCode with equals?

### Related

[hashcode.md](./hashcode.md) · [hash-buckets.md](./hash-buckets.md) · [hashmap.md](./hashmap.md)
