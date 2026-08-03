# Track: Low-Level Design (LLD)

## What they test

Requirements → types → invariants → interfaces at variation points → concurrency → evolution. Not UML decoration.

## PCR-OTDR mapping

| Spine | LLD move |
|-------|----------|
| Problem | Use cases |
| Context | Single process? scale? |
| Reasoning | Invariants / ownership |
| Options | Strategies, state models |
| Trade-offs | Complexity vs flexibility |
| Decision | Concrete types + ports |
| Result | Test plan + “add X” blast radius |

## Practice sources

- [../../low-level-design](../../low-level-design/)  
- Depth pack: [../low-level-design](../low-level-design/)  
- Formats: [../formats/mock-interviews/lld-45.md](../formats/mock-interviews/lld-45.md) · [../formats/architecture/parking-lot-evolve.md](../formats/architecture/parking-lot-evolve.md)

## Loop

Design aloud 25 min → “add payments” 5 min → list files touched.
