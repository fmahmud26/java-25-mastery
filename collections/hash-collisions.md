# Hash Collisions

Distinct keys map to the same bucket index (or same hash).

## How It Works

Inevitable with finite tables. Resolved by chaining (list/tree). Cost: more `equals` calls.

## Production

Attack / poor hash → DoS-like CPU (mitigated by treeify + better hashes historically). Still: don’t ignore key quality.

## Trade-offs

Tree bins bound worst-case per bin to O(log n) but add constant overhead and complexity.

## Interview

- Collision resolution in HashMap?  
- Treeify thresholds (≈8, capacity ≥64)?  
- **Staff:** how do you detect collision problems in prod?

### Related

[hashing.md](./hashing.md) · [hashmap.md](./hashmap.md)
