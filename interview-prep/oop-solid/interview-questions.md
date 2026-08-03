# OOP + SOLID — Interview Questions (L1–L4)

Escalate from definitions → trade-offs → production design diagnosis.

---

## Level 1 — Junior

### What are the four pillars of OOP?

**Answer:** Encapsulation (hide state), Abstraction (essential interface), Inheritance (subtype/reuse), Polymorphism (one interface, many behaviors). In Java: classes/interfaces, `extends`/`implements`, overriding.

### Encapsulation vs abstraction?

**Answer:** Encapsulation is about bundling and access control (private fields). Abstraction is about exposing a useful model while hiding complexity (interfaces/APIs). Related but not the same.

### What does SOLID’s S mean?

**Answer:** Single Responsibility — a class should have one primary reason to change. Example: don’t mix payment charging, email sending, and report generation in one service class.

---

## Level 2 — Mid-level

### Composition vs inheritance — when which?

**Answer:** Prefer composition for “has-a” and pluggable behavior; use inheritance for true “is-a” that preserves Liskov. Inheritance couples you to the base; composition keeps variation behind interfaces.

### Explain Open/Closed with a Java example.

**Answer:** Depend on `Pricing`; add `TieredPricing` without editing `CheckoutService`. OCP is about extension points (polymorphism/strategies), not “never edit any file.”

### What is Dependency Inversion?

**Answer:** High-level policy depends on abstractions, not concretions. Inject `PaymentGateway`; don’t `new StripeClient()` inside domain logic. Enables testing and swapping providers.

---

## Level 3 — Senior

### How do you detect LSP violations?

**Answer:** Subtypes that throw unexpected exceptions, ignore methods (`UnsupportedOperationException` on collections), strengthen preconditions, or weaken postconditions. Classic: Square extending Rectangle with setter invariants broken.

### Records and sealed types — how do they change OOP design?

**Answer:** Records give concise immutable data; sealed hierarchies close the world so pattern switches are exhaustive — data-oriented design alongside classic OOP services. Prefer modeling variants as sealed types rather than deep mutable inheritance.

### When is SOLID harmful?

**Answer:** Premature interfaces, one-impl abstractions, and SRP taken to “one method per class.” Apply SOLID at real variation/test boundaries; measure complexity of the design itself.

---

## Level 4 — Expert

### Legacy monolith: 4k-line `OrderManager` owns pricing, inventory, payments, emails. p99 rising and every change breaks something. How do you approach it?

**Answer (structured):**

1. **Confirm symptoms** — change failure rate, hot methods in profiles, test gaps.  
2. **Map reasons to change** — list SRP seams (pricing vs payment vs notify).  
3. **Stabilize** — characterization tests around current behavior; freeze API.  
4. **Extract ports** — `Pricing`, `Inventory`, `PaymentGateway`, `Notifier` interfaces; move logic behind them (Strangler).  
5. **DIP wiring** — inject impls; stop static singleton soup.  
6. **Concurrency / perf** — isolate shared mutable order state; immutable snapshots where needed.  
7. **Validate** — deploy behind feature flags; compare error budgets and p99; add architecture fitness tests (dependency rules).

**Common Mistake at L4:** Proposing a full rewrite or “just microservices” without seams, tests, or a strangler plan.

---

## Extra ladder prompts (practice)

| L | Prompt |
|---|--------|
| 1 | Abstract class vs interface? |
| 2 | Method overloading vs overriding? |
| 3 | How would you design extensible notification channels? |
| 4 | Subtype hierarchy breaking production invariants under load — diagnose |
