# Arrays

Fixed-length, contiguous (conceptually) sequences of primitives or references.

## 1. Mental Model

```text
int[] cents = new int[3];   // length fixed at creation
cents[0] = 100;             // O(1) index
```

## 2. Simple Explanation

An array has a fixed `length`. Indexes are `0 .. length-1`. Arrays are objects (reference type) even when they hold primitives. Prefer `List` for growing collections; arrays for fixed buffers, interop, and low-level performance.

## 3. Technical Explanation

- Creation: `new T[n]`, initializer `{...}`, `new T[]{...}`  
- Multidimensional: arrays of arrays (ragged allowed)  
- Covariance: `String[]` is `Object[]` — store check can throw `ArrayStoreException`  
- `clone` is shallow for object arrays  
- Utilities: `Arrays.sort`, `copyOf`, `asList` (fixed-size view)

## 4. Internal Behavior

Header + continuous slots (JVM layout). Bounds check on access → `ArrayIndexOutOfBoundsException`. Passing arrays shares the reference — callee can mutate contents.

## 5. Java 25 Example

```java
long[] dailyTotalsCents = new long[31];
dailyTotalsCents[day] += amountCents;

String[] headers = {"Authorization", "X-Request-Id"};
List<String> view = Arrays.asList(headers); // fixed size; set ok, add throws
```

## 6. Real-World Scenario

**Batch export:** service built growing results with `ArrayList` then toArray once. An older path used manual `System.arraycopy` resizing — harder to read, same asymptotics. Kept List until serialization boundary.

## 7. Common Mistake

Assuming arrays grow; forgetting length is field not method; mutating shared array args; using `Arrays.asList` then `add`.

## 8. Failure Scenario

`ArrayIndexOutOfBoundsException` in prod from off-by-one in paging. Fix bounds + tests; prefer collections with clear size APIs when dynamic.

## 9. Performance Implications

Primitive arrays avoid boxing. Random access is O(1). Large arrays = large contiguous heap demand → allocation failures / GC pressure. Don’t micro-prefer arrays over List without measurement.

## 10. Interview Questions

- Array vs ArrayList?  
- Why ArrayStoreException?

## 11. Senior-Level Follow-ups

- When keep `byte[]` buffers in a network server?  
- Covariance pitfalls vs generics?

## 12. Principal Engineer Perspective

Use arrays as **buffers and FFI-ish shapes**; use collections for domain modeling. Treat shared mutable arrays like shared mutable objects — dangerous across threads.

### Related

[reference-types.md](./reference-types.md) · [loops.md](./loops.md) · [../collections](../collections/)
