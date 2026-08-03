# Cross-Team Architecture

Architecture across teams fails when **ownership and runtime coupling** disagree.

## Coupling inventory (do this literally)

| Coupling | Example | Hazard |
|----------|---------|--------|
| Shared DB tables | Two services write `users` | Uncoordinated migrations |
| Sync chatty RPC | Checkout → 8 services | Latency multiplication |
| Shared libraries (logic) | “Common” domain jar | Version lockstep |
| Shared mobile contract | One BFF for all | Release coupling |
| Events without schema | Free JSON | Poison consumers |

## PE moves

- Assign **single writer** for each aggregate  
- Prefer async + published contracts at boundaries  
- Platform owns paved paths; product owns domain  
- Explicit “anti-corruption” adapters when legacy leaks  

## Overlap symptom

Two services expose overlapping APIs and both page for the same user bug. Fix boundaries, not more meetings alone.

Related: [system-boundaries.md](./system-boundaries.md), [scenarios/overlapping-services.md](../scenarios/overlapping-services.md).
