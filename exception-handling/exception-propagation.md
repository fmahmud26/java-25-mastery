# Exception Propagation

How failures travel up the stack until handled or kill the thread/task.

## Mental Model

```text
controller
  └─ CheckoutService
       └─ PaymentPort.capture()  throws PaymentTransientException
            └─ HttpPspClient     SocketTimeoutException
                 ↑ translated ↑ propagates ↑ mapped to 503 + Retry-After
```

Unchecked: propagate without `throws`. Checked: must declare or catch each hop.

## Failure Propagation Strategies

| Strategy | When |
|----------|------|
| **Propagate** | No useful recovery here |
| **Translate then propagate** | Cross a layer boundary |
| **Absorb + fallback** | Rare; must be explicit product decision |
| **Absorb + return Result** | Expected business outcomes |

## Bad vs Improved — payment failure

```java
// Bad — catch at bottom, return null
catch (Exception e) { return null; }

// Bad — catch at top only as Exception with no classification
catch (Exception e) { return 500; }

// Improved
// client: throw/translate transient
// service: let propagate or retry policy
// controller: map PaymentTransientException → 503
//             PaymentRejectedException → 422
```

## Production — message consumer

```java
try {
    handler.handle(msg);
    ack();
} catch (TransientException e) {
    nackBackoff(e);      // redelivery
} catch (PermanentException e) {
    log.error("poison", e);
    archiveToDlq(msg, e); // don’t infinite retry
    ack();
}
```

## Principal / Resilience

Propagation policy **is** your failure domain design. Undefined catch locations → random retries and duplicate charges. Document per boundary: HTTP, messaging, batch.

### Related

[exception-translation.md](./exception-translation.md) · [error-handling-strategy.md](./error-handling-strategy.md) · [retry-decisions.md](./retry-decisions.md)
