# Hash Buckets

Table slots holding colliding entries.

## How It Works

Each index is a **bucket**: empty, single node, linked list, or tree (HashMap). Lookup compares hash then `equals` along the bin.

## Memory / Iteration

Many small buckets → table memory; few huge bins → CPU. Iteration walks all bins + chains.

## Failure

One bucket owns most keys → profile shows deep `HashMap.get`. Fix hashing; consider different key space.

## Interview

- What is a bucket?  
- When does a bin treeify?

### Related

[hash-collisions.md](./hash-collisions.md) · [hashmap.md](./hashmap.md)
