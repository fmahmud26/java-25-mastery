# Reference Types

Objects, arrays, and interfaces — variables hold **references**, not the object bytes.

## 1. Mental Model

```text
Order order ──► [Order @ heap]
Order alias ──┘   same object, two names
```

## 2. Simple Explanation

A reference type variable stores a pointer-like reference (or `null`). Assignment copies the reference, not a deep clone. Mutating through one alias is visible through others.

## 3. Technical Explanation

- Classes, interfaces, arrays, enums, records, annotations.  
- Equality: `==` identity; `equals` value contract (override carefully).  
- **String:** immutable UTF-16/`byte[]` compact strings; pool for literals; prefer `equals`; build with `StringBuilder` / careful `+` outside hot loops; never use strings as locks.  
- Generics erase to references (not primitives historically).

## 4. Internal Behavior

Objects live on the heap (escape analysis may scalar-replace). GC reclaims unreachable objects. `null` deref → `NullPointerException`. String concat in loops can allocate many intermediates unless builder/optimized.

## 5. Java 25 Example

```java
final class Customer {
    private final String id;
    Customer(String id) { this.id = id; }
    String id() { return id; }
}

Customer a = new Customer("C-1");
Customer b = a;                 // alias
String s = "paid";              // interned literal
String t = new String("paid");  // usually distinct object
```

## 6. Real-World Scenario

**Order aggregate** returned from a repository was mutated by a controller “for convenience,” corrupting cache. Fix: immutable DTOs / defensive copies at boundaries.

## 7. Common Mistake

Assuming `=` clones; comparing Strings with `==`; mutating shared collections returned from services.

## 8. Failure Scenario

Intermittent wrong totals because two threads share a mutable `Order`. Fix ownership rules + immutability. Prevent: records/unmodifiable views at API edges.

## 9. Performance Implications

Allocation + GC dominate careless object churn. String building in hot paths needs builders or careful concat. Identity maps (`IdentityHashMap`) when `==` semantics are required.

## 10. Interview Questions

- What does a reference store?  
- `==` vs `equals` for String?

## 11. Senior-Level Follow-ups

- How do you design immutable API boundaries?  
- When is defensive copy worth the cost?

## 12. Principal Engineer Perspective

Treat aliasing as a **concurrency and integrity** hazard. Prefer immutable payloads across threads and process boundaries.

### Related

[primitive-types.md](./primitive-types.md) · [variables.md](./variables.md) · [arrays.md](./arrays.md) · [final.md](./final.md)
