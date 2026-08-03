# Low-Level Design — Theory

LLD = turn a vague product prompt into **types, responsibilities, extension points** — not a microservice map.

## Interview skeleton (30–45 min)

1. Clarify scope (actors, must-haves, single JVM vs distributed).  
2. Nouns → types (entities, values, services, ports).  
3. Verbs → flows (hottest use cases).  
4. Contracts at variation points.  
5. Concurrency: what is shared?  
6. Evolve: “add X” without rewrite.

## Design axes

| Axis | Question |
|------|----------|
| SOLID | Where are change boundaries? |
| Composition | Can behavior be plugged in? |
| Patterns | Strategy/Factory/Observer — only if they earn their keep |
| Thread safety | Immutable vs locked vs concurrent collections |
| Testability | Can you fake ports? |

## LLD vs SD vs coding

| Layer | Focus |
|-------|-------|
| Coding problems | Algorithms/DS |
| **LLD** | Classes/APIs in one (or few) processes |
| System design | Services, data stores, networks |

Related: [README.md](../../low-level-design/README.md), [solid.md](../../low-level-design/concepts/solid.md), [oop.md](../../low-level-design/concepts/oop.md).
