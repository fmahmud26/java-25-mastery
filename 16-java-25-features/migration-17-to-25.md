# Migration: Java 17 → 25

Two LTS jumps: absorb **21** headlines first, then **22–25**.

## Phased Approach

### Phase A — Think in 21 terms

- Virtual threads readiness (pinning, pools, DB connections)  
- Pattern switch / record patterns  
- Sequenced collections  

### Phase B — Absorb 22–24

- FFM final; Class-File API; Stream Gatherers  
- ZGC generational-only path; VT pinning improvements  
- Security Manager removal; integrity tightening  

### Phase C — Land on 25

- Scoped Values final  
- Language: module import, compact source, flexible constructors  
- KDF API  
- Optional: compact headers, AOT ergonomics, JFR upgrades  
- Avoid preview dependencies  

## Risk Register

| Risk | Mitigation |
|------|------------|
| Huge behavioral surface | Intermediate CI on 21 then 25 |
| Native/agents | Early smoke on 25 EA/GA |
| GC change | Keep collector stable first; change second |
| Training | Interview/LTS evolution doc for the team |

### Related

[migration-21-to-25.md](./migration-21-to-25.md) · [lts-evolution.md](./lts-evolution.md) · [jeps-since-21.md](./jeps-since-21.md)
