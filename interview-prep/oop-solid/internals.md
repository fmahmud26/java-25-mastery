# OOP + SOLID — Internals

Interviewers rarely want JVM bytecode dumps — they want **dispatch & contract mechanics**.

## Method dispatch

| Kind | Resolved how |
|------|----------------|
| `static` / private / `final` / constructors | Compile-time (invokestatic / invokespecial) |
| Instance override | Runtime virtual dispatch (`invokevirtual`) |
| Interface methods | Interface dispatch (`invokeinterface`); defaults live on the interface |
| `record` accessors | Final components; generated equals/hashCode/toString |

## Inheritance realities

- Single class inheritance; multiple interface inheritance.
- Fields are not polymorphic — only methods (and covariant returns).
- Constructor chaining: `this(...)` / `super(...)` before body; object not fully constructed until subclass finishes.

## SOLID mapped to mechanics

| Principle | Runtime / design lever |
|-----------|------------------------|
| OCP | New implementing type + polymorphic call site |
| LSP | Behavioral contract of overridden methods |
| DIP | Call through interface; DI wires implementation |
| Sealed | Compiler exhaustiveness on pattern switch |

## Pitfalls

- Overriding without matching `equals`/`hashCode` contracts.
- Exposing mutable internals → broken encapsulation.
- “Inheritance for reuse” of unrelated utilities → brittle base class.

Related: [method-overriding.md](../../oop/method-overriding.md), [inheritance.md](../../oop/inheritance.md), [constructor.md](../../oop/constructor.md).
