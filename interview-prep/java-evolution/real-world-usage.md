# Java Evolution — Real-World Usage

| Scenario | Choice | Why |
|----------|--------|-----|
| New domain model | Records + sealed | Safer APIs, less mutability |
| Blocking microservice I/O | Virtual threads (21/25) | Scale without reactive rewrite |
| Shared library on multiple JDKs | Multi-release / oldest supported LTS | Compatibility contract |
| Migration 17 → 25 | Read release notes + dep audit | Removed APIs, reflection, agents |
| Pattern-heavy parsers | Pattern switch | Exhaustiveness vs visitor boilerplate |
| Team skills | Adopt features incrementally | Style guide > feature tourism |

## Production rules of thumb

- Upgrade **JDK** and **deps** together; run tests + canary.  
- Prefer finalized features for core paths; isolate previews.  
- Document “language level” in build (`release`/`--release 25`).  
- Train the team on records/sealed/VT — review culture matters more than syntax.

Related: [migration-17-to-25.md](../../16-java-25-features/migration-17-to-25.md), [migration-21-to-25.md](../../16-java-25-features/migration-21-to-25.md).
