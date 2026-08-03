# Engineering Trade-offs

| Decision | Prefer when | Avoid when |
|----------|-------------|------------|
| Immutability everywhere | Shared values, concurrency | Tight mutable high-churn with measured cost |
| Optional returns | Maybe-one finders | Every field in JSON DTOs |
| Sealed results | Closed domain outcomes | Open plugin SPI |
| Typed errors | Expected business flows | Truly exceptional bugs |
| Heavy logging | Boundaries & milestones | Per-row DEBUG in hot loops |
| Feature flags | Gradual rollout | Permanent unowned flags |
| Shared library | True cross-cutting | Fake reuse creating coupling |

PE move: write the trade-off in the PR / ADR — don’t pretend there’s only one “clean” answer.

### Related

[principal-decisions.md](./principal-decisions.md) · [maintainability.md](./maintainability.md)
