# Parallel streams doing blocking I/O

## Question

`urls.parallelStream().map(this::httpGet)` saturates the common ForkJoinPool and starves unrelated parallel work. Diagnosis and fix?

## Difficulty

Senior

## Expected answer

Parallel streams use the shared FJP—blocking holds worker threads. Use virtual threads / dedicated executor for blocking I/O; keep parallel streams for CPU-bound, pure computations on large in-memory data.

## Reasoning

FJP designed for CPU tasks; blocking reduces parallelism and causes pool exhaustion cascades.

## Follow-up

How do you isolate parallel CPU work from the common pool?

## Common mistake

“Parallel means faster” on network calls.

## Principal-level discussion

Standard: ban blocking in `parallelStream`. Provide VT HTTP client templates. Incident reviews treat FJP starvation as a platform smell.
