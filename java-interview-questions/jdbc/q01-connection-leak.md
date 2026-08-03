# Pool exhausted; DB CPU idle

## Question

Hikari waits spike; DB CPU low. Threads blocked on `getConnection`. Causes?

## Difficulty

Mid

## Expected answer

Leaked connections (not closed), long transactions holding connections, pool too small vs concurrency, or slow queries holding connections. Fix leaks (try-with-resources), shorten tx, size pool, timeouts.

## Common mistake

Blaming “DB is down” when pool is empty.

## Follow-up

What pool metrics do you alert on?
