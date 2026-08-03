# Payment System

**Assumption:** payment **orchestration** service inside your company: customers, merchants, providers (Stripe/Adyen). Not rebuilding card networks.

## Requirements

- Charge / refund / (optional) capture-auth  
- **Idempotency** on all money side effects  
- Multiple providers behind one port  
- Ledger or journal of attempts/outcomes for audit  
- Safe retries; no double charge on client timeout  
- Webhooks from provider to update status  

**Non-goals:** PCI-DSS full card vault design lecture (mention tokenization boundary).

## Use cases

1. Create payment intent / charge  
2. Retry / resume unknown outcome  
3. Refund full/partial  
4. Handle provider webhook  
5. Query status by `paymentId` / `idempotencyKey`  

## Domain model

| Concept | Invariant |
|---------|-----------|
| `Payment` | Unique business id; state INITIATED/AUTHORIZED/CAPTURED/FAILED/REFUNDED… |
| `IdempotencyKey` | Same key + same merchant → same logical payment |
| `PaymentAttempt` | Provider call record; outcome |
| `Money` | Currency-safe; no float |
| `LedgerEntry` | Append-only audit |

**Why attempts ≠ payment:** retries create multiple provider calls under one business payment.

## Classes

| Class | Responsibility | Why |
|-------|----------------|-----|
| `PaymentService` | Orchestrate | API |
| `Payment` aggregate | State transitions | Invariants |
| `IdempotencyStore` | Key → payment id | Dedupe |
| `ProviderRouter` | Choose provider | Strategy |
| `Ledger` | Append events | Audit |
| `WebhookHandler` | Map provider events | Edge |

## Interfaces

| Port | Why |
|------|-----|
| `PaymentProvider` | `charge`, `refund`, `parseWebhook` |
| `IdempotencyStore` | Redis/DB later |
| `LedgerStore` | Persistence |
| `Clock` | |

**Contract on `PaymentProvider`:** must support caller idempotency key pass-through where provider allows.

## Relationships

```text
PaymentService → IdempotencyStore
PaymentService → ProviderRouter → PaymentProvider
PaymentService → Ledger
WebhookHandler → PaymentService.applyProviderEvent
```

## SOLID

- **OCP:** new PSP = new adapter  
- **SRP:** routing ≠ ledger ≠ webhook auth  
- **LSP:** providers honor idempotent charge semantics  
- **DIP:** domain never imports Stripe SDK types  

## Design patterns

| Pattern | Where | Why |
|---------|-------|-----|
| Strategy / Router | Provider selection | Geo, cost, failover |
| State | Payment lifecycle | Illegal refund etc. |
| Adapter | PSP SDKs | Anti-corruption |
| Outbox (mention) | Reliable webhook side-effects | At-least-once elsewhere |

## Thread safety

- Idempotency record insert must be atomic (unique constraint)  
- Payment state transitions compare-and-swap  
- Webhook + client confirm concurrent → terminal state monotonic (never CAPTURED→INITIATED)  

## Error handling

| Failure | Behavior |
|---------|----------|
| Client retry same key | Return original payment view |
| Provider timeout | Leave PENDING; reconcile via query/webhook — **do not auto-recharge blindly** |
| Webhook duplicate | Idempotent apply |
| Insufficient funds | FAILED; allow new payment new key |
| Partial refund over remaining | Reject |

## Extensibility

| Change | Touch |
|--------|-------|
| Auth + capture | New states + provider methods |
| Multi-rail (wallet) | New provider adapter |
| Split payments | New domain rules + ledger lines |

## Testing

- Double submit same idempotency key → one charge mock call  
- Webhook before sync response → still CAPTURED once  
- Refund limits  
- Adapter unit tests map PSP errors to domain errors  

## Trade-offs

| Choice | Why | Rejected |
|--------|-----|----------|
| Business payment + attempts | Accurate retries | One row per HTTP call as source of truth — messes client API |
| Provider port | Vendor lock-in control | Calling Stripe in every service |
| Pending on timeout | Safety | Immediate retry charge — double spend risk |
| Append-only ledger | Audit | Mutable balance field only — disputes hard |

**Staff emphasis:** **idempotency + pending reconciliation** is the interview. Class diagram without that fails.
