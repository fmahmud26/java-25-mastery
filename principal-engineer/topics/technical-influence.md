# Technical Influence

Influence is **changing the default path of code and traffic**, not winning Slack arguments.

## Mechanisms that work

| Mechanism | Why technical |
|-----------|----------------|
| ADR + measured spike | Replaces opinion with evidence |
| Golden path template | Makes right thing easiest |
| Canary showing SLO win | Data beats narrative |
| Error budget policy | Forces prioritization |
| Office hours on architecture | Scale yourself |
| Codeowners on sensitive paths | Enforce review where irreversible |

## Mechanisms that fail

- Status meetings without decisions  
- Soft guidelines on idempotency after a double-charge  
- Designing forever without a cutover date  

## PE interview signal

Tell a story: “We proved dual-write lag < 500ms for 14 days, then deleted the old path — incidents fell N%.”

Related: [architectural-decision-making.md](./architectural-decision-making.md), [influence.md](../influence.md).
