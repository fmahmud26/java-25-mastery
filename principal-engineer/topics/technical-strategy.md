# Technical Strategy

Multi-year sequencing of **bets** that compound — not a roadmap of features.

## Strategy = constraints + bets + sequencing

```text
Business constraint (e.g. enter EU, 10× merchants)
  → Technical constraint (data residency, write path ceiling)
    → Bets (shard key, regional cells, platform checkout)
      → Sequence (observe → fix ceiling → expand geography)
```

## Good strategy artifacts

| Artifact | Technical content |
|----------|-------------------|
| North-star architecture diagram | Target boundaries + data flow in 24 months |
| Bottleneck backlog | Ranked by risk to SLO/$ |
| Platform vs product split | What is mandatory shared |
| Migration waves | Wave 0 safety → wave N features |

## Anti-strategy

- “Rewrite in X” without strangler path  
- Copying FAANG topology at 1% scale  
- Strategy that ignores on-call load and cost  

## PE test

Can an EM/staff engineer predict what you’ll say “no” to next quarter? If not, you have wishes, not strategy.

Related: [architecture-evolution.md](./architecture-evolution.md), [system-boundaries.md](./system-boundaries.md).
