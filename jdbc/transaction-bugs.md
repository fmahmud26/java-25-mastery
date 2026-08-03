# Transaction Bugs

## Common Failure Modes

| Bug | Result |
|-----|--------|
| Auto-commit left on for multi-step | Partial persistence |
| Forget `rollback` on error | Dirty session / pool poison |
| Commit then “maybe” do more DML | Second part outside tx |
| Long tx holding locks | Deadlocks / latency |
| Catch swallow + commit | Silent corruption |
| HTTP to PSP inside DB tx | Pool exhaustion + fragile locks |

## Payment Example (bad)

```text
begin
  update order set status=PAID
  call stripe.charge()   // network inside tx
  insert payment_row
commit
```

If Stripe succeeds and JVM dies before commit → money taken, DB not paid. Prefer: insert intent → commit → charge → update with idempotency / outbox.

## Inventory Example (bad)

Decrement stock without checking `qty >= ?` → negative stock under concurrency.

## Fix Patterns

- Explicit `setAutoCommit(false)` + commit/rollback in one place  
- Conditional updates with rowcount checks  
- Outbox / saga for external systems  
- Timeouts on statements  

### Related

[transactions.md](./transactions.md) · [deadlocks.md](./deadlocks.md) · [scenarios.md](./scenarios.md)
