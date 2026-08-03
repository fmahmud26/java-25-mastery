# Architectural Decision Making

Principals don’t collect options — they **force a decide-by date** with reversible vs irreversible called out.

## Decision classes (technical)

| Class | Example | Process |
|-------|---------|---------|
| **One-way door** | Data model for money ledger; tenant isolation boundary | Written ADR, small review board, spike proof |
| **Two-way door** | Cache library, queue client wrapper | Team decides; document lightly |
| **Policy** | “No sync call from checkout to recommendations” | Standard + lint/arch unit test |

## ADR skeleton (keep technical)

1. Context (load, SLO, ownership)  
2. Decision  
3. Alternatives rejected **with cost**  
4. Consequences (ops, migrations, APIs)  
5. Follow-up date / revisit triggers  

## Inputs that must appear

- Numbers: QPS, data size, error budget, $  
- Failure mode: what breaks first  
- Ownership: who pages at 3am  
- Reversibility: how hard to undo  

## Anti-patterns

- Consensus theater with no owner  
- “Temporary” dual writes with no delete date  
- Architecture by vendor slide deck  
- Deciding microservices before finding the bottleneck  

Related: [trade-offs.md](./trade-offs.md), [technical-strategy.md](./technical-strategy.md).

**Worked examples (portfolio):** [../portfolio/README.md](../portfolio/README.md) — VT, payment PENDING, outbox, no premature microservices.
