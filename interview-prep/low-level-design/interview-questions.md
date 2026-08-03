# Low-Level Design — Interview Questions (L1–L4)

Types and trade-offs beat pattern name-dropping.

---

## Level 1 — Junior

### What is low-level design in interviews?

**Answer:** Designing classes, interfaces, and collaborations for a feature/system module — responsibilities, APIs, and how pieces interact — usually within one application process.

### Composition vs inheritance in LLD?

**Answer:** Prefer composition to plug behaviors (pricing, allocation). Use inheritance only for true subtype relationships that honor Liskov.

### Why introduce an interface in LLD?

**Answer:** To mark a **variation point** (strategy, port) so you can extend/test without editing callers. Not every class needs an interface.

---

## Level 2 — Mid-level

### How do you approach a Parking Lot design?

**Answer:** Clarify vehicle/spot types, fee model, concurrency. Types: `ParkingLot`, `Spot`, `Ticket`, `Vehicle`, `Pricing`. Index free spots by type; park/leave flows; discuss thread safety and extending spot types via strategy/config.

### Where do design patterns fit?

**Answer:** After responsibilities are clear. Strategy for policies, Factory for complex creation, Observer for events. Patterns are tools — justify each.

### How do you make LLD testable?

**Answer:** Inject ports (`Clock`, `PaymentGateway`, `Inventory`); prefer pure domain methods; avoid static singletons and hidden `new` of collaborators.

---

## Level 3 — Senior

### How do you handle concurrency in a ticket booking LLD?

**Answer:** Define the invariant (seat not double-booked). Options: lock per show/seat, DB unique constraint + optimistic version (if persistence in scope), reservation TTL state machine. In pure in-memory: fine-grained locks or atomic CAS on seat state.

### God class smell mid-design — what do you do?

**Answer:** Split by reason to change (SRP): allocation vs pricing vs notification. Keep a thin facade if the interviewer wants one entry API.

### When should LLD stop and become system design?

**Answer:** When answers need multiple services, networks, shared DBs, or cross-region consistency. Say so explicitly and offer a bridge (“this interface becomes a remote API”).

---

## Level 4 — Expert

### Production bug: in-memory rate limiter works in tests, but multi-instance deploy allows 3× traffic. Design diagnosis?

**Answer (structured):**

1. **Confirm** — per-instance limits vs global intent; traffic pattern; which keys.  
2. **Root cause** — local `TokenBucket` state not shared across instances.  
3. **Options**  
   - Redis/token service for global limit  
   - Gateway rate limit  
   - Sticky sessions (usually weak for this)  
4. **LLD fix** — `RateLimiter` port with `Local` and `Distributed` implementations (DIP).  
5. **Correctness** — define window semantics, clock skew, fail-open vs fail-closed.  
6. **Validate** — integration tests with two nodes; chaos on Redis; metrics on allow/deny.

**Common Mistake at L4:** Only tweaking bucket math while ignoring multi-instance state.

---

## Extra ladder prompts (practice)

| L | Prompt |
|---|--------|
| 1 | Entity vs value object? |
| 2 | Design a vending machine state model |
| 3 | Extending elevator scheduling policy |
| 4 | Cache stampede inside a single JVM library — diagnose |
