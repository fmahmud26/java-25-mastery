# Interview — Stream API

Problems + engineering judgment for **Java 25**. Detail in topic files.

---

## Conceptual

| Q | Sketch |
|---|--------|
| Lifecycle? | create → lazy intermediates → one terminal |
| Lazy why? | defer work; short-circuit; fuse stages |
| Intermediate vs terminal? | Stream vs result/effects |
| map vs flatMap? | 1:1 vs 1:many flatten |
| reduce vs collect? | immutable fold vs mutable collector |
| Stream vs loop? | [stream-vs-loop.md](./stream-vs-loop.md) |
| Parallel pitfalls? | I/O on FJP, races, non-associative reduce |

---

## Coding problems

### 1) GMV by customer (orders)

`Map<String, Long>` of paid order totals by customer id.  
→ `filter` + `groupingBy` + `summingLong`

### 2) All SKUs sold

From `List<Order>` with lines → distinct SKUs.  
→ `flatMap` + `map` + `distinct`

### 3) Highest-paid per department

→ `groupingBy` + `maxBy` + `collectingAndThen` (see classic employee problem)

### 4) Risk partition

Split txs by `cents >= threshold` with counts.  
→ `partitioningBy` + `counting`

### 5) Customer index

`List<Customer>` → `Map<String, Customer>`  
→ `toMap` + merge policy if duplicates

### 6) First ERROR for service in log file

→ `Files.lines` + `filter` + `findFirst` + try-with-resources

### 7) Total cents without boxing

→ `mapToLong` + `sum`

---

## Principal scenarios

1. Teammate adds `.parallel()` on an order enrichment pipeline that calls HTTP — what do you do?  
2. Report OOMs on `groupingBy` — how diagnose cardinality?  
3. When do you reject streams in code review for a payment capture flow?  
4. Top-10 orders: stream `sorted+limit` vs DB vs heap — decide.  
5. Where do side effects go in a stream-heavy analytics job?

---

## Quick answers

- Reuse stream? **No**  
- `peek` in prod? **Avoid**  
- `findFirst` vs `findAny`? Order vs parallel-friendly  
- `toList()` mutable? **No** (unmodifiable)  
- Money in `DoubleStream`? **Prefer long cents**

### Related

[README.md](./README.md) · [grouping-by.md](./grouping-by.md) · [parallel-streams.md](./parallel-streams.md) · [flatmap.md](./flatmap.md)
