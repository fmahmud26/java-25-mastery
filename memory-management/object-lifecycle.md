# Object Lifecycle

From allocation to reclamation — what “alive” means operationally.

## Mental Model

```text
Allocated (new)
  → In use (strongly reachable)
  → Softly / weakly / phantom reachable (optional stages)
  → Unreachable
  → Reclaimed (GC)
  → (optional) cleanup via Cleaner / phantom queue
```

## Technical Mechanism

| Stage | Meaning |
|-------|---------|
| Created | Constructor finished; reference published or local |
| Reachable | Strong path from GC root |
| Collectible | No strong path (soft/weak policies apply) |
| Finalization | Legacy — avoid |
| Phantom / Cleaner | Post-mortem side effects |
| Reused memory | Eden/old region reused for new objects |

```java
byte[] buf = new byte[4096]; // allocated
process(buf);                // in use
buf = null;                  // may become unreachable if no other refs
// GC later reclaims
```

## JVM Internals

- Short-lived objects: allocate in Eden, die in young GC — cheap.  
- Long-lived: promote to old — more expensive to collect.  
- Accidental longevity (caches, listeners) turns young garbage into old live set.

## Production Implications

Design for **short lifetimes** on request paths. Anything that outlives a request needs an explicit retention policy ([object-retention.md](./object-retention.md)).

## Interview / PE

When does an object become eligible for GC? Does setting a field to `null` always free memory immediately? (No — only removes a ref; GC runs later; other refs may remain.)

### Related

[object-allocation.md](./object-allocation.md) · [object-retention.md](./object-retention.md) · [references.md](./references.md)
