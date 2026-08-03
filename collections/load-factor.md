# Load Factor

Threshold ratio controlling when a hash table **resizes**.

## How It Works

Default HashMap **0.75**: resize when `size > capacity × loadFactor`. Higher → less memory, longer bins; lower → more memory, fewer collisions.

## Production

Leave 0.75 unless measuring. Size with `HashMap.newHashMap(expectedMappings)` to reduce early resizes.

## Trade-offs

| Lower LF | Higher LF |
|----------|-----------|
| More table space | Denser, more collisions |
| Fewer long bins | More CPU per lookup |

## Interview

- What is load factor?  
- Why 0.75 default?  
- How does it trigger rehash?

### Related

[rehashing.md](./rehashing.md) · [hashmap.md](./hashmap.md)
