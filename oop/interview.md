# Interview — OOP (Principal Drill)

Answer with a **domain story**, then the rule. Depth lives in linked notes.

---

## Composition vs inheritance

| | Inheritance | Composition |
|--|-------------|-------------|
| Relationship | *is-a* | *has-a* |
| Coupling | To parent API | To part’s small API |
| Extension | Fixed tree | Swap/decorate parts |
| Risk | Fragile base, LSP breaks | More wiring |

**Default:** composition. Inheritance for true subtypes / sealed ADTs.

**PE follow-ups:** Who owns lifecycle of the part? How do you test without the real PSP? What breaks when the base class adds a method?

See [composition.md](./composition.md) · [inheritance.md](./inheritance.md)

---

## Interface vs abstract class

| | Interface | Abstract class |
|--|-----------|----------------|
| Multiplicity | Many | One `extends` |
| State | No instance fields | Fields + ctors |
| Role | Capability / port | Template + shared state |

**Rule:** boundary APIs → interface; shared algorithm skeleton with fields → abstract class (sparingly); else compose a helper.

See [interfaces.md](./interfaces.md) · [abstract-classes.md](./abstract-classes.md)

---

## Overloading vs overriding vs dynamic dispatch

| | Overloading | Overriding |
|--|-------------|------------|
| Binding | Compile-time | Runtime (dispatch) |
| Signature | Different params | Same params |
| PE risk | Wrong overload | LSP violation |

Dynamic dispatch: JVM picks override from **runtime class** of the receiver.

See [polymorphism.md](./polymorphism.md) · [method-overloading.md](./method-overloading.md) · [method-overriding.md](./method-overriding.md)

---

## Encapsulation & immutability

- Encapsulate **invariants**, not just fields.  
- `final` reference ≠ deep immutability.  
- Records: values/messages; entities: encapsulated transitions.

See [encapsulation.md](./encapsulation.md) · [immutability.md](./immutability.md) · [records.md](./records.md)

---

## Association / aggregation / composition

| | Association | Aggregation | Composition |
|--|-------------|-------------|-------------|
| Meaning | Uses / knows | Whole–part, shared life | Owns part |
| Backend tip | IDs + ports | Snapshot foreign data | Private final deps |

See [association.md](./association.md) · [aggregation.md](./aggregation.md) · [composition.md](./composition.md)

---

## Sealed vs open polymorphism

- **Sealed + switch:** closed domain facts (events, commands) — exhaustiveness.  
- **Open interfaces:** adapters/plugins (Stripe, email providers).

See [sealed-classes.md](./sealed-classes.md) · [domain-modeling.md](./domain-modeling.md)

---

## LLD-style scenarios (practice aloud)

### 1) Checkout (order + payment + inventory)

Design types for cart → pay → reserve stock → confirm.  
**Probes:** aggregate root? money type? ports? failure compensation? composition of policies?

### 2) Notification fan-out

Order paid → email + push + SMS.  
**Probes:** inheritance tree vs `List<Notifier>`? retries? ISP for channels that lack templates?

### 3) Banking ledger

Deposit/withdraw/transfer.  
**Probes:** where does balance live? immutable entries? encapsulation of overdraft rules?

### 4) Inventory reservation

Reserve on checkout, release on payment timeout.  
**Probes:** entity vs service? illegal states? idempotency?

### 5) Logistics shipment statuses

**Probes:** string statuses vs sealed hierarchy? who may transition? audit?

### 6) Payment PSP adapters

**Probes:** interface vs abstract adapter base? decorator for metrics? how add Apple Pay without editing checkout?

---

## Principal Engineer question bank

1. How do you decide ownership between `Order` and `PaymentIntent` services?  
2. What coupling do you accept between domain model and JPA?  
3. When is a deep inheritance tree a maintenance incident waiting to happen?  
4. How do you keep domain logic testable without Spring?  
5. How do you evolve a sealed `PaymentEvent` hierarchy across producers/consumers?  
6. Where does extensibility live — new subclass, new adapter, or new message type?  
7. How do you prevent anemic models without over-engineering CRUD?  
8. What is your review bar for `public` setters on domain types?

---

## Quick facts

- Constructors are **not** inherited.  
- `static` methods are **not** overridden (hiding).  
- Prefer `@Override` always.  
- “Must call `super`” is a design smell — use final templates.  
- Illegal states should be **hard to represent**.

### Related

[README.md](./README.md) · [domain-modeling.md](./domain-modeling.md)
