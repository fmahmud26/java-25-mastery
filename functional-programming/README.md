# Functional Programming in Java 25

Practical FP on the JVM: **SAM types, lambdas, composition, and purity discipline** — not Haskell cosplay. Java stays object-oriented; FP is a tool for clearer APIs and safer data transforms.

**Standards:** [DEEP_LEARNING_STANDARD.md](../DEEP_LEARNING_STANDARD.md) · [MASTER_INSTRUCTION.md](../MASTER_INSTRUCTION.md)

## Study path

1. Foundations: [functional-interfaces](./functional-interfaces.md) → [lambda-expressions](./lambda-expressions.md) → [method-references](./method-references.md)  
2. Core SAMs: [Predicate](./predicate.md) · [Function](./function.md) · [Consumer](./consumer.md) · [Supplier](./supplier.md) · [UnaryOperator](./unary-operator.md) · [BinaryOperator](./binary-operator.md) · [BiFunction](./bi-function.md) · [BiConsumer](./bi-consumer.md)  
3. Composition: [function-composition](./function-composition.md) → [and-then](./and-then.md) · [compose](./compose.md) · [higher-order-functions](./higher-order-functions.md)  
4. Discipline: [effectively-final](./effectively-final.md) · [closures](./closures.md) · [side-effects](./side-effects.md) · [immutability](./immutability.md)  
5. Drill: [interview.md](./interview.md) · [when-to-use.md](./when-to-use.md)

## When FP improves design

| Situation | Why |
|-----------|-----|
| Policy injection (pricing, fraud, notify) | Swap behavior without subclass trees |
| Collection transforms | Declarative filter/map with clear intent |
| Lazy defaults / retry suppliers | Defer work |
| Composing small pure steps | Testable units |

## When FP makes code worse

| Situation | Why |
|-----------|-----|
| Multi-step business workflow with branching | Long lambda chains hide control flow |
| Heavy checked exceptions / transactions | Awkward in SAMs |
| Hot loops with boxing | `Stream<Integer>` tax |
| Debuggability critical | Stack traces show `lambda$...` |
| Team can’t read composition | Clever `compose` chains vs named methods |

## Principal stance

Prefer **pure functions at the core**, side effects at the edges. Lambdas for short SAMs; named methods/classes when logic grows. Measure before blaming “streams are slow.”
