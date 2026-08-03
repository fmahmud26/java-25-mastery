# sorted / distinct / limit / skip / peek

## sorted

**What:** Stateful ordering (natural or `Comparator`).  
**Why:** Ranked reports.  
**Prod:** Top orders by cents — prefer DB/`PriorityQueue` for huge n.  
**Perf:** O(n log n), buffers.  
**Mistake:** Sorting before filter.

## distinct

**What:** Drops duplicates via equals/hashCode.  
**Why:** Unique SKUs/customers.  
**Perf:** Stateful set memory.  
**Mistake:** Mutable elements / broken hashCode.

## limit / skip

**What:** Slice the stream.  
**Why:** Top-N, pagination-like (prefer DB offset for big data).  
**Perf:** Ordered parallel limit is costly.  
**Mistake:** `skip` huge n in-app.

## peek

**What:** Side-effecting spy intermediate.  
**Why:** Debug only.  
**Mistake:** Business logic in `peek`.

### Related

[stateful-operations.md](./stateful-operations.md) · [side-effects.md](./side-effects.md)
