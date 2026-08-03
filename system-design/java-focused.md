# Java-Focused Notes for System Design

Use when the interviewer cares about JVM services — don’t let this replace architecture.

## Service shape

- Stateless Spring Boot / Micronaut / Helidon behind LB  
- **Virtual threads** for high-concurrency blocking I/O (DB, HTTP clients) on Java 21+; still size DB pools  
- Explicit timeouts on `HttpClient`, JDBC, Redis — no infinite blocks  

## Data access

- Connection pool budget: `replicas × pool ≤ db max`  
- Outbox table + publisher thread/CDC  
- Idempotency rows with unique constraint  

## Caching

- Caffeine L1 + Redis L2  
- Singleflight via per-key locks or Caffeine `LoadingCache`  

## Messaging

- Kafka consumers: commit after side effects carefully; idempotent handlers  
- Prefer clear keys for ordering (`orderId`)  

## Resilience

- Resilience4j / custom: retry budgets, CB, bulkhead thread pools per dependency  
- Deadline propagation via headers  

## Observability

- Micrometer metrics, OpenTelemetry tracing, structured JSON logs with `traceId`  

## What not to over-index

- Don’t design the whole system around Reactive Streams unless team already lives there — VT + clear pools is a valid PE answer on Java 25.
