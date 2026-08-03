# OOP + SOLID — Theory

## Four pillars

| Pillar | Contract | Java cue |
|--------|----------|----------|
| Encapsulation | Hide state; expose behavior | private fields, getters/records accessors |
| Abstraction | Essential interface, hide details | interfaces, abstract classes, sealed hierarchies |
| Inheritance | Reuse + subtype relationship | `extends` — prefer shallow trees |
| Polymorphism | Same message, different behavior | override, interface dispatch |

## Composition vs inheritance

| Prefer | When |
|--------|------|
| **Composition** | “has-a”, pluggable behavior, avoid fragile base |
| **Inheritance** | True “is-a”, shared contract, Liskov-safe |

## SOLID (interview shortlist)

| Letter | Principle | Smell if violated |
|--------|-----------|-------------------|
| **S** | Single Responsibility | God class; many reasons to change |
| **O** | Open/Closed | Endless `if`/`switch` edits for new variants |
| **L** | Liskov Substitution | Subtype throws / weakens preconditions |
| **I** | Interface Segregation | Fat interfaces; clients depend on unused methods |
| **D** | Dependency Inversion | Concrete `new` deep in domain; untestable statics |

## Java 25 modeling tools

- **Records** — immutable data carriers (encapsulation without boilerplate).
- **Sealed classes/interfaces** — closed hierarchies (great with pattern `switch`).
- Prefer **interfaces + composition** over deep inheritance for domain variation.

Related: [encapsulation.md](../../oop/encapsulation.md), [polymorphism.md](../../oop/polymorphism.md), [solid.md](../../modern-java-engineering/solid.md).
