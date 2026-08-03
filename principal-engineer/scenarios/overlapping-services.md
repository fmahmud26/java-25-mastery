# Scenario: Two Teams Own Overlapping Services

## Context

Team **Identity** owns `user-service`. Team **Growth** built `profile-service` last year for experiments. Both expose `GET/PATCH /users/{id}`, both write to overlapping columns on the same `users` table (email, display_name, marketing_flags). Mobile apps call both. Incidents ping both on-calls; schema migrations collide monthly. A GDPR delete took 3 days because neither team owned erasure end-to-end.

## Constraints

- Cannot stop feature delivery for a full quarter  
- Must preserve mobile backward compatibility for 2 app versions  
- Legal requires deterministic delete/export SLA (30 days → goal 48h)  
- Single Postgres cluster today; no greenfield rewrite budget  

## Options

| Option | Approach |
|--------|----------|
| **A. Status quo + coordination** | Shared Slack, migration calendar |
| **B. Single writer API** | Identity owns writes; Growth reads via API/events |
| **C. Merge services** | One deployable, two module owners |
| **D. Split table + sync** | `accounts` vs `growth_profiles` with events |
| **E. BFF-only consolidation** | Hide overlap at edge; leave two writers |

## Trade-offs

| Option | Buys | Sells |
|--------|------|-------|
| A | Zero eng | Collisions continue; compliance risk |
| B | Clear invariant ownership | Growth loses direct SQL speed |
| C | One schema owner | Org conflict; large PR risk |
| D | Cleaner domains | Dual-run complexity; eventual consistency on profile |
| E | App simplicity | **Does not fix dual writers** — masks smell |

## Decision

**B then D:** Immediately enforce **single-writer** through Identity for identity fields; Growth gets read APIs + CDC/events for marketing attributes. Then physically split marketing columns into `growth_profiles` owned by Growth.

## Reasoning

Overlapping **HTTP** is cosmetic; overlapping **writers** are the SEV factory. Compliance needs one authority for deletion graphs. Edge BFFs without write ownership leave the race intact.

## Risks

- Hidden writers (jobs, admin scripts) bypass API  
- Eventual consistency: Growth UI shows stale display_name  
- Mobile still calling deprecated PATCH on profile-service  
- Performance: Growth N+1 through Identity  

## Migration

1. Inventory all writers (DB grants, ORM mappings, jobs) — revoke direct Growth WRITE on identity columns.  
2. Expand: Identity API supports needed Growth fields temporarily **or** proxy writes.  
3. Ship mobile to Identity for identity patches; feature-flag.  
4. Contract: profile-service PATCH returns 410 for moved fields.  
5. CDC: Identity → Kafka `UserUpdated`; Growth projects what it needs.  
6. Move `marketing_flags` to `growth_profiles`; dual-read; drop columns.  
7. GDPR workflow: Identity orchestrates delete; Growth consumer must ack.  

## Success metrics

- Exactly one service with INSERT/UPDATE grants on identity columns  
- Schema migration conflicts → 0 per quarter  
- GDPR delete p95 ≤ 48h; single runbook owner  
- Dual on-call pages for same user bug → 0  
- Deprecated profile PATCH traffic → 0 within 2 app releases  

Related: [../topics/cross-team-architecture.md](../topics/cross-team-architecture.md), [../topics/system-boundaries.md](../topics/system-boundaries.md).
