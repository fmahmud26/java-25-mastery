# Interview Difficulty Levels

Every interview-questions file in a topic pack uses **four levels**. Answer at the level asked; offer to go deeper if they nod.

## Level 1 — Junior

**Goal:** Correct vocabulary and safe basic usage.

- What is X?
- When do you use List vs Set?
- Write a simple put/get example.

**Signal you’re ready for L2:** You never confuse interface vs implementation (`Map` vs `HashMap`).

## Level 2 — Mid-level

**Goal:** Explain *how* it works and pick the right structure.

- Internals (buckets, trees, load factor)
- Complexity (amortized vs worst case)
- Thread-safety boundaries

**Signal for L3:** You can draw the data structure and name failure modes without notes.

## Level 3 — Senior

**Goal:** Predict degradation, design around it, teach trade-offs.

- Why does performance collapse under X?
- HashMap vs ConcurrentHashMap vs per-thread maps
- API design: mutable keys, equals/hashCode contracts

**Signal for L4:** You’ve related the topic to an incident or load-test story.

## Level 4 — Expert

**Goal:** Production diagnosis and remediation under uncertainty.

- Symptoms → hypotheses → tools (JFR, async-profiler, `jcmd`, metrics)
- Fix options ranked by risk (shard keys, striping, change structure, cache)
- How you’d validate the fix and prevent recurrence

**Anti-pattern:** Jumping to “use ConcurrentHashMap” without evidence of contention.

## How interviewers escalate

```text
L1 What is HashMap?
 → L2 How does it work internally?
 → L3 When does it get slow / unsafe?
 → L4 Here’s a production graph — diagnose it.
```

Practice answering **one level fully** before volunteering the next.
