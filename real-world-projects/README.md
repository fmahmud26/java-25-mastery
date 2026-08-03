# Real-World Projects — Progressive Java 25 Track

Build **production-shaped** systems in increasing difficulty. Each project is a design + implementation exercise you can defend in a Principal interview — not a tutorial CRUD blog.

## Requirements

- **JDK 25** (`javac --release 25`)
- Prefer plain `javac`/`run.sh` where present; later projects may note optional Testcontainers/JMH

## Progression

| Tier | # | Project | Forces you to learn |
|------|---|---------|---------------------|
| Foundation | [01](./01-cli-application/) | Structured log analyzer CLI | Clean module boundaries, testing, exit contracts |
| Concurrency | [02](./02-multithreaded-application/) | Concurrent order processor | VT, locks, atomics, CF composition |
| Edge service | [03](./03-http-service/) | Resilient HTTP service | Networking, timeouts, retries, metrics |
| Async core | [04](./04-event-processing/) | Idempotent event pipeline | Backpressure, graceful shutdown, repo port |
| Measurement | [05](./05-performance-lab/) | Concurrency performance lab | Benchmark honesty, VT vs platform |
| Persistence | [06](./06-url-shortener-service/) | URL shortener + rate limit | Clean arch, JDBC, cache, scalability |
| Resilience | [07](./07-payment-orchestrator/) | Payment orchestrator | Idempotency, CB, unknown outcomes |
| Scale path | [08](./08-notification-outbox/) | Notification outbox platform | Outbox, workers, observability, fan-out |

## Required doc template (every project)

```text
Problem → Requirements → Architecture → Technology choices
→ Design decisions → Implementation plan → Failure scenarios
→ Testing strategy → Performance considerations → Scaling strategy
→ Interview discussion
```

## How to use

1. Read the project README end-to-end **before** coding extensions.  
2. Implement/extend against the plan; run failure drills listed.  
3. For 06–08: `bash run-tests.sh` (JDK 25) — assertion suites, no Maven.  
4. Rehearse **Interview discussion** aloud with metrics + [../principal-engineer/portfolio/](../principal-engineer/portfolio/) ADRs.  
5. Only then move to the next tier — each assumes prior skills.

## What “good” looks like

- Ports at I/O boundaries; domain free of Servlet/JDBC types where claimed  
- Virtual threads on blocking I/O; pools sized for DB  
- Timeouts, idempotency, and metrics on every networked path  
- Tests for concurrency races and failure injection — not only happy path  
