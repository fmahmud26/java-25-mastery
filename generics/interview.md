# Interview — Generics (Hard)

Answer with **rule → example → erasure consequence**. Depth in topic files.

---

## Must-know

### PECS

Producer `? extends T`, consumer `? super T`, both → exact `T`.  
[pecs.md](./pecs.md)

### Type erasure — why and so what?

Java erased generics for **migration compatibility** and a simple runtime model. Consequences: no `new T()`, no `List<String>.class`, no overload on type args only, need type tokens at boundaries.  
[type-erasure.md](./type-erasure.md) · [limitations.md](./limitations.md)

### `List<?>` vs `List<Object>`

| | `List<?>` | `List<Object>` |
|--|-----------|----------------|
| From `List<String>` | Assignable | Not assignable |
| add | Only `null` | Any Object |
| Meaning | Unknown element type | Homogeneous Object list |

### `? extends` vs `? super`

Read as `T` vs write `T`; get on `super` is `Object`.

### Why no generic arrays?

Arrays reified + covariant; generics erased + invariant — combination allows heap pollution if generic arrays were freely allowed.

---

## Difficult questions (practice aloud)

1. Write `copy` with correct wildcards; infer `T` for `List<Integer>` → `List<Number>`.  
2. Why is `<T super Number>` illegal on a type parameter?  
3. Explain a bridge method from an override example.  
4. How does Jackson read `List<Order>` despite erasure?  
5. Why can’t a class implement `Comparable<A>` and `Comparable<B>`?  
6. Is `String[]` a subtype of `Object[]`? Is `List<String>` a subtype of `List<Object>`? Why the inconsistency?  
7. Fix an API `void register(List<Event> e)` that won’t accept `List<PaymentEvent>`.  
8. Where is `@SuppressWarnings("unchecked")` justified vs a design smell?  
9. Design `Repository<ID,E>` with PECS on a `search` method that accepts criteria producers.  
10. What is heap pollution? Show a raw-type sequence ending in CCE.  
11. Why return `List<T>` not `List<? extends T>` from a public finder?  
12. How do `Enum<E extends Enum<E>>` self-bounds help?

---

## Principal discussion prompts

- When do you require `Class<T>` vs hiding tokens behind codegen?  
- How do generics + sealed events interact for exhaustiveness vs handlers?  
- What’s your review bar for unchecked casts in a payments codebase?  
- Invariance vs business “is-a” — how do you educate teams?

---

## Quick hits

| Q | A |
|---|---|
| Raw type? | Avoid — legacy hole |
| Multiple bounds order? | Class first, then interfaces |
| Diamond? | Infer type args from context |
| Erasure of `<T extends Number>`? | To `Number` |
| PECS on returns? | Usually don’t |

### Related

[README.md](./README.md) · [pecs.md](./pecs.md) · [type-erasure.md](./type-erasure.md) · [limitations.md](./limitations.md)
