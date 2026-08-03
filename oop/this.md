# `this`

Reference to the **current instance** — disambiguate fields, call sibling ctors, pass the receiver.

## 1. Mental Model

```text
inside instance method:  this  →  the receiver object
```

## 2. Problem It Solves

Parameter names shadow fields; constructors need to delegate; fluent APIs return the same instance carefully.

## 3. Bad Design → Problems → Better Design

**Bad:** Leak `this` from a constructor to a shared registry/thread before subclass init finishes.

**Problems:** Other threads see partially constructed objects (especially with overrides).

**Better:** Publish only from a factory after full construction; don’t start threads in ctors.

```java
public final class Shipment {
    private final String id;
    private ShipmentStatus status;

    public Shipment(String id) {
        this.id = Objects.requireNonNull(id); // disambiguate
        this.status = ShipmentStatus.CREATED;
    }

    public Shipment markInTransit() {
        this.status = ShipmentStatus.IN_TRANSIT;
        return this; // fluent — OK if immutable alternative not required
    }
}
```

## 4. Technical Rules (Java 25)

- `this()` ctor call must be first statement (historically; flexible ctor bodies evolve — prefer simple chaining).  
- Not available in static context.  
- Inner classes: `Outer.this` for enclosing instance.

## 5. Internal Behavior

`this` is local #0 in instance methods/ctors (bytecode). Escaping `this` early breaks safe publication assumptions.

## 6. Domain Scenarios

- **Orders:** builder/`this` chaining for line adds on a mutable draft; publish immutable `Order` at end.  
- **Banking:** avoid registering `this` account into a static map inside ctor.

## 7. Trade-offs & When Not

Fluent `return this` on mutable objects shares aliases — prefer immutable `withX` returning new instances for values.

## 8. Failure Scenario

Listener registered with `this` in ctor calls overridden method on subclass — subclass fields still default. Fix: factory + `init()` or final class.

## 9. LLD Interview Scenario

Is a fluent mutable `Cart` OK? When freeze into immutable `CartSnapshot` for pricing?

## 10. SOLID / Extensibility

Leaking `this` increases coupling (observers hold your concrete type). Prefer publishing domain events instead of registering `this`.

## 11. Interview Ladder

- `this` vs `super`?  
- Why is escaping `this` in ctor dangerous?  
- `this()` chaining rules?

## 12. Principal Engineer Perspective

Treat early `this` publication as a concurrency defect until proven otherwise. Prefer completed, valid objects before sharing.

### Related

[super.md](./super.md) · [constructor.md](./constructor.md) · [constructor-chaining.md](./constructor-chaining.md)
