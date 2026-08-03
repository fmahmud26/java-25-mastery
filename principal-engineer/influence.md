# Influence Playbook (Technical)

How Principals move systems without line authority.

## Sequence that works

1. **Instrument the pain** — SLO burn, cost/$order, MTTR, dual-write lag.  
2. **Spike a reversible fix** — 1–2 weeks, production canary.  
3. **Write the ADR** — options killed with numbers.  
4. **Pave the road** — template/CI so the next team inherits the win.  
5. **Set a delete date** — old path removal on calendar.  

## Saying no (technically)

“No new sync dependency on checkout until p99 budget shows ≥80ms headroom; propose async or cache.”

## Saying yes with conditions

“Yes to service split after: single-writer inventory API, contract tests, and shadow traffic 7 days at <0.1% diff.”

## Organizational interface

You negotiate **error budgets, platform mandates, and migration windows** — still in technical terms.
