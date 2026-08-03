# Minor Collection (Young GC)

Collection focused on the **young generation** (Eden + survivors / young regions).

## Mental Model

```text
Trigger: Eden cannot satisfy allocation
Action:  evacuate live young objects; promote some to old
Result:  Eden empty for fresh allocation
```

Also called young GC / minor GC. In G1 logs: young-only collections.

## Technical Mechanism

- Typically **STW** for the collection pause (duration depends on live survivors + collector).  
- Dead young objects are not swept individually — space is reused after survivors are copied.  
- Remembered sets / cards find pointers from old → young.

## Production Implications

| Frequent young GC | Often high allocation rate or small young capacity |
| Long young pauses | Many survivors to copy; sizing / premature tenuring |
| OK pattern | Steady young GCs with low pause and stable old occupancy |

## Interview / PE

What triggers minor GC? Why usually cheaper than full? Relation to allocation rate?

### Related

[major-gc.md](./major-gc.md) · [young-generation.md](./young-generation.md) · [allocation-rate.md](./allocation-rate.md)
