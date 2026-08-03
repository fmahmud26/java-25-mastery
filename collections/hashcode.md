# hashCode

Integer for hashing — distributes keys across buckets.

## Rules

Consistent with equals; cheap; stable while object is in a hash structure. Don’t use mutable fields that change after insert.

## Internals Link

HashMap spreads bits then masks to bucket — [hashing.md](./hashing.md).

## Interview

- 31* in classic String hash?  
- Records’ generated hashCode?  
- **Staff:** measuring hash quality under load?

### Related

[equals.md](./equals.md) · [hash-collisions.md](./hash-collisions.md)
