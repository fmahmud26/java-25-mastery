# Scenario: Platform Adoption Stall

## Context

Platform team built a golden-path service template (Java 25, Micrometer, OTel, CI deploy, Resilience4j defaults). After 9 months only 20% of new services use it. Teams cite: “template too opinionated,” “build slow,” “doesn’t support Gradle customizations,” and “we needed Python.” Meanwhile SEVs show missing timeouts on hand-rolled clients. Platform measured by “services created,” not adoption quality. Exec asks PE to “make platform succeed.”

## Constraints

- Cannot mandate rewrite of all legacy  
- Three languages in prod (Java, Python, Node)  
- Platform staffing: 6 engineers  
- Must reduce SEV class “missing timeout/retry storm”  

## Options

| Option | Approach |
|--------|----------|
| **A. Mandate template org-wide** | Block prod without it |
| **B. Thin the golden path** | Defaults library ≠ full archetype |
| **C. Multi-language paved roads** | |
| **D. Abandon platform; guild docs** | |
| **E. Embed platform engineers in product** | |

## Trade-offs

| Option | Buys | Sells |
|--------|------|-------|
| A | Compliance | Shadow IT; revolt if path bad |
| B | Adoption | Less “complete” |
| C | Coverage | Dilutes staffing |
| D | Peace | SEVs continue |
| E | Adoption locally | Platform doesn’t scale |

## Decision

**B + narrow A + C-lite:** Split platform into:
1. **Mandatory security/reliability libraries** (HTTP client defaults, auth, telemetry) versioned per language — enforced in CI for any prod deploy.  
2. **Optional opinionated template** for Java happy path — optimize build time.  
3. Python/Node: **same library contracts**, not full template parity year one.  
Change platform KPIs to: % traffic covered by hardened clients; SEV rate from retry/timeout; time-to-first-deploy on golden path.

## Reasoning

Adoption stalled because platform optimized for **one archetype** while the org needed **non-negotiable defaults**. Mandating a fat template fails; mandating timeout/idempotency/telemetry wins. PE success is traffic covered by safe defaults, not repo count.

## Risks

- Library upgrades break teams → need semver + canary  
- “Mandatory” without support queue → malicious compliance  
- Template still ignored — OK if libraries stick  

## Migration

1. Extract Resilience+OTel+auth from template into `platform-http-java` (+ py/node thin wrappers).  
2. CI policy: prod pipelines fail without approved client BOM version.  
3. Fix Java template build (cache, lighter deps); publish migration guide.  
4. Office hours; embed for 2 top teams to dogfood.  
5. Kill vanity metrics; dashboard coverage %.  

## Success metrics

- ≥90% of edge/service HTTP calls use approved clients in 2 quarters  
- SEVs with root cause “no timeout/retry storm” ↓ ≥75%  
- New Java services on template ≥60% (secondary)  
- Median time-to-first-prod on golden path ≤ 1 day  
- Platform NPS qualitative up; bypass tickets down  

Related: [../topics/platform-engineering.md](../topics/platform-engineering.md), [../topics/engineering-standards.md](../topics/engineering-standards.md), [../topics/technical-influence.md](../topics/technical-influence.md).
