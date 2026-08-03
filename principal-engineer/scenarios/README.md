# Scenarios Index

Realistic Principal Engineer decision scenarios. Each uses:

Context → Constraints → Options → Trade-offs → Decision → Reasoning → Risks → Migration → Success metrics

| Scenario | Decision essence |
|----------|------------------|
| [scale-100x](./scale-100x.md) | Bottleneck-first; don’t microservice a DB ceiling |
| [overlapping-services](./overlapping-services.md) | Single writer before org therapy |
| [legacy-blocks-growth](./legacy-blocks-growth.md) | Strangle the billing invariant, don’t rewrite all |
| [zero-downtime-migration](./zero-downtime-migration.md) | CDC + cohort cutover; avoid eternal dual write |
| [microservices-not-required](./microservices-not-required.md) | Modular monolith + extract proven seams |
| [cost-up-5x](./cost-up-5x.md) | Attribute $; reverse premature active-active |
| [reliability-declining](./reliability-declining.md) | Shorten critical path; error budgets; retry standards |
| [shared-database](./shared-database.md) | Separate data planes via CDC projections |
| [platform-adoption-stall](./platform-adoption-stall.md) | Mandate libraries, not fat templates |
| [virtual-threads-no-gain](./virtual-threads-no-gain.md) | VT for I/O concurrency — not free CPU |
| [connection-pool-exhaustion](./connection-pool-exhaustion.md) | Size pools from DB hold time, not threads |
| [cpu-saturation](./cpu-saturation.md) | Profile before threads/VT/hardware |
| [memory-growth](./memory-growth.md) | After-GC heap → path-to-root → bounds |
| [gc-pause-growth](./gc-pause-growth.md) | Measure alloc/live set before collector swap |
| [thread-explosion](./thread-explosion.md) | Fix executor topology; bound fan-out |
| [latency-cliff](./latency-cliff.md) | Timeouts + bulkheads; no retry storms |
