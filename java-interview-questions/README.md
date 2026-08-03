# Java Interview Question Bank

Serious, **scenario-first** prompts for Java interviews — not trivia flashcards.

Complementary depth tracks: [interview-prep](../interview-prep/), [experiments](../experiments/), [principal-engineer](../principal-engineer/).

## Difficulty ladder

| Level | What we test |
|-------|----------------|
| **Junior** | Correct mental model; safe defaults |
| **Mid** | APIs + failure modes; when not to use X |
| **Senior** | Production diagnosis; trade-offs under load |
| **Staff** | Cross-system effects; standards; “what breaks at 10×” |
| **Principal** | Strategy, invariants, org-scale technical judgment |

## Template

**Junior / Mid**

```text
Question · Difficulty · Expected answer · Common mistake · Follow-up
```

**Senior / Staff / Principal** (required)

```text
Question · Difficulty · Expected answer · Reasoning
· Follow-up · Common mistake · Principal-level discussion
```

## Categories

| Category | Path |
|----------|------|
| Core Java | [core-java](./core-java/) |
| OOP | [oop](./oop/) |
| Collections | [collections](./collections/) |
| Generics | [generics](./generics/) |
| Streams | [streams](./streams/) |
| Concurrency | [concurrency](./concurrency/) |
| Virtual Threads | [virtual-threads](./virtual-threads/) |
| JVM | [jvm](./jvm/) |
| Memory | [memory](./memory/) |
| GC | [gc](./gc/) |
| Performance | [performance](./performance/) |
| Java 25 | [java-25](./java-25/) |
| Design Patterns | [design-patterns](./design-patterns/) |
| JDBC | [jdbc](./jdbc/) |
| Networking | [networking](./networking/) |
| Security | [security](./security/) |
| Testing | [testing](./testing/) |

## Design / PE (lives in sibling folders)

This bank is **language/runtime-heavy**. For design interviews use:

| Need | Go to |
|------|--------|
| LLD systems | [../low-level-design/](../low-level-design/) |
| System design + DS | [../system-design/](../system-design/) |
| PE scenarios | [../principal-engineer/scenarios/](../principal-engineer/scenarios/) |
| Prep tracks | [../interview-prep/tracks/](../interview-prep/tracks/) |
| Cheat sheets | [../cheat-sheets/](../cheat-sheets/) |
| Incidents | [../scenario-lab/](../scenario-lab/) |

Drill guide: [interview.md](./interview.md)

## How to practice

1. Cover the question; answer aloud in 2–5 minutes using [PCR-OTDR](../interview-prep/answer-framework.md).  
2. Uncover **Expected answer**; note gaps.  
3. Answer **Follow-up** without peeking.  
4. For Staff/Principal: rehearse **Principal-level discussion** as if with an architect panel.
