# Young Generation

Space (or set of regions) for **newly allocated** objects.

## Mental Model

```text
Young
├── Eden          ← typical new allocations (via TLAB)
├── Survivor S0
└── Survivor S1
```

```text
Young collection:
  copy live Eden + from-survivor → to-survivor (or promote to old)
  dead objects are abandoned (no sweep of dead young objects)
```

## Technical Mechanism

| Space | Role |
|-------|------|
| Eden | Bump-pointer / TLAB allocation |
| Survivors | Objects that lived through at least one young GC |
| Promotion threshold | Age / to-space shortage → old |

G1: Eden/Survivor are **region sets**, not one contiguous pair of spaces — same idea.

## Production Implications

- Eden fills → young/minor collection (see [minor-gc.md](./minor-gc.md)).  
- Very large objects may bypass Eden (humongous / old paths) — collector-specific.  
- Young pause time correlates with **live survivors copied**, not Eden size alone.

## Interview / PE

Why copy rather than sweep young? What happens when survivors overflow?

### Related

[old-generation.md](./old-generation.md) · [allocation.md](./allocation.md) · [minor-gc.md](./minor-gc.md)
