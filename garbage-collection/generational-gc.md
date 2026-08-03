# Generational GC

**Weak generational hypothesis:** most objects die young. Split work so young collections stay cheap.

## Mental Model

```text
new → Young (Eden) → survive collections → Old (long-lived)
         ↑
    most death happens here → frequent, small collections
```

## Technical Mechanism

| Idea | Effect |
|------|--------|
| Young gen | High death rate; copying/evacuation of *survivors* only |
| Old gen | Lower death rate; more expensive when collected |
| Promotion | Objects that live long enough move old |

Region-based collectors (G1) and modern low-pause collectors still exploit this idea (G1 always; ZGC generational on Java 25; Shenandoah optionally generational).

## JVM Internals

- Age bits in object headers track survival count (layout collector/JVM dependent).  
- Remembered sets / card tables track old→young pointers so young collections need not scan the entire old gen.  
- Premature promotion (too-small young gen or huge survivors) pollutes old gen → more old/mixed/full work.

## Production Implications

- High allocation of short-lived objects is **normal**; watch **promotion rate** and **after-GC old occupancy**.  
- “Optimize by disabling generational” is almost never the answer — measure first.

## Trade-offs

Small young gen → frequent young GC, less promotion delay. Large young gen → fewer young GCs, longer young pauses possible, more survivors to copy. Collector ergonomics usually manage this — don’t random-tune ratios without logs.

## Interview / PE

State the generational hypothesis. What is promotion? Why track old→young refs?

### Related

[young-generation.md](./young-generation.md) · [old-generation.md](./old-generation.md) · [allocation.md](./allocation.md)
