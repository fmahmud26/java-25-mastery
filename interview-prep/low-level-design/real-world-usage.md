# Low-Level Design — Real-World Usage

| Scenario | Choice | Why |
|----------|--------|-----|
| Pricing / fees | Strategy + DI | OCP for business rules |
| Device/protocol adapters | Ports & adapters | Test without hardware |
| In-process cache | Explicit capacity + policy | Avoid accidental unbounded `HashMap` |
| Workflow states | Sealed states / explicit enum FSM | Illegal transitions fail fast |
| Multi-threaded booking | Lock per resource / optimistic version | Correct seat inventory |
| Feature flags | Strategy swap | Evolve without rewrite |

## Production rules of thumb

- Draw the **module boundary** you’ll actually own.  
- Prefer fewer, clearer types over pattern catalogs.  
- Always answer thread-safety and test fakes.  
- Call out what belongs in SD instead (Kafka, multi-region).

Related: [../../modern-java-engineering/domain-modeling.md](../../modern-java-engineering/domain-modeling.md), [../../design-patterns](../../design-patterns/).
