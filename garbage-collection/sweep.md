# Sweep

After marking, **sweep** reclaims memory occupied by unmarked objects.

## Mental Model

```text
marked live → keep
unmarked    → free for reuse (free lists / bump spaces)
```

## Technical Mechanism

Pure mark-sweep can **fragment** the heap (free holes between live objects). Compaction/evacuation fixes that ([compact.md](./compact.md)).

Young generations often skip classic sweep: they **copy** survivors and abandon the rest of Eden.

## Production Implications

Fragmentation symptoms: allocation failures despite free bytes; humongous allocation failures (G1). Compacting collectors/phases address this at a pause or concurrent cost.

### Related

[mark.md](./mark.md) · [compact.md](./compact.md) · [full-gc.md](./full-gc.md)
