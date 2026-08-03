# Connection Leaks

## Problem

Connections taken from the pool are never returned → pool drains → app hangs.

## Symptoms

- Threads blocked on `getConnection`  
- Pool metrics: active = max, idle = 0  
- Eventually timeouts / “Connection is not available”

## Naive causes

```java
Connection c = ds.getConnection();
// exception before close
doWork(c);
c.close(); // skipped
```

Or: store Connection in a static/ThreadLocal; pass across threads; hold during long I/O.

## Fix

```java
try (Connection c = ds.getConnection()) {
    doWork(c);
}
```

## Prevention

- Try-with-resources everywhere  
- Hikari `leakDetectionThreshold` in staging  
- Never hold connection while calling Stripe/PayPal  
- Code review for `getConnection` without try-with  

## Debug

[debugging.md](./debugging.md) — pool JMX + thread dump showing waiters on pool.

### Related

[connection-pooling.md](./connection-pooling.md) · [pool-exhaustion.md](./pool-exhaustion.md)
