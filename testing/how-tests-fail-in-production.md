# How Tests Fail in Production

Green CI, red production — common patterns.

| Gap | What happened | Fix |
|-----|---------------|-----|
| **Over-mocking** | Mocked DB/HTTP; real SQL/JSON broke | Integration / contract tests |
| **Happy-path only** | Missed null, empty, timezone, leap | Parameterized edges |
| **Wrong environment** | Tests on H2; prod Postgres | Testcontainers matching prod engine |
| **Flaky ignored** | Race in tests; race in prod too | Fix or delete; quarantine with owner |
| **No migration test** | Schema drift | Migrate in IT |
| **Time/locale** | Passed in UTC CI, failed elsewhere | Inject Clock; fix locale |
| **Unrealistic data** | Tiny strings; prod Unicode/emoji | Realistic fixtures |
| **Missing authz tests** | IDOR in prod | Security tests with principal |
| **Config not tested** | Feature flag combo untested | Slice tests for config |
| **Load/concurrency** | Unit single-threaded | Concurrent IT for inventory |

## PE Response

Treat production bugs as **test design defects** when appropriate — add the missing layer, don’t only hotfix.

### Related

[what-not-to-mock.md](./what-not-to-mock.md) · [principal-strategy.md](./principal-strategy.md)
