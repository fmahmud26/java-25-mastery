# Test Coverage

## Use

Find **untested critical paths** — not to hit a arbitrary percentage.

| Good | Bad |
|------|-----|
| Coverage on `checkout`, `refund`, authz | 95% by testing getters |
| Diff coverage on PR | Blocking on line % alone |
| Mutation testing sparingly | Ignoring integration gaps |

## PE Stance

Report coverage for hot modules; enforce on money paths; never game with useless tests.

### Related

[principal-strategy.md](./principal-strategy.md) · [what-to-unit-test.md](./what-to-unit-test.md)
