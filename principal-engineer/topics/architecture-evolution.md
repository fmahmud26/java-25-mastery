# Architecture Evolution

Systems evolve by **compatible steps**, not by announcing a new diagram.

## Mechanisms

| Mechanism | Technical meaning |
|-----------|-------------------|
| Expand/contract | Add column/API field → dual read → remove old |
| Strangler façade | New impl behind old interface; traffic shift |
| Branch by abstraction | Seam in code before storage change |
| Event carving | Extract read model before extracting service |
| Cell architecture | Split blast radius by tenant/region |

## Evolution triggers (measure them)

- Single primary CPU > N% at peak with no cache win left  
- Change failure rate up when touching module M  
- On-call pages dominated by one subsystem  
- Cost per order up without product reason  

## PE rule

Never evolve org chart and data model in the same unmeasured step. Sequence: **observe → seam → dual-run → cutover → delete**.

Related: [migration-strategy.md](./migration-strategy.md), [scenarios/legacy-blocks-growth.md](../scenarios/legacy-blocks-growth.md).
