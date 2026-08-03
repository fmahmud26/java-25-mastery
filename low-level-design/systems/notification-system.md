# Notification System

**Assumption:** in-process notification **dispatcher** used by product services. Fan-out to email/SMS/push/in-app via adapters. Not designing the global Kafka company bus unless asked.

## Requirements

- Send notification to a user across one or more channels  
- Templates with parameters  
- Preference / opt-out respect  
- Retry with backoff on transient failure  
- At-least-once delivery to providers; idempotent provider keys where possible  
- Pluggable channels without editing publishers  

**Non-goals:** full ESP deliverability team tooling; marketing campaign builder.

## Use cases

1. Publish `NotificationRequest` (user, template, data, urgency)  
2. Resolve channels from prefs + routing policy  
3. Dispatch per channel; record status  
4. Retry failures; dead-letter after N  
5. Admin suppress / preference update  

## Domain model

| Concept | Invariant |
|---------|-----------|
| `Notification` | Id; payload; target user |
| `Channel` | EMAIL/SMS/PUSH/IN_APP |
| `DeliveryAttempt` | Per channel try outcome |
| `UserPreferences` | Opt-outs honored before send |
| `Template` | Validated required params |

**Why attempt history:** support/debug and idempotent retries need it.

## Classes

| Class | Responsibility | Why |
|-------|----------------|-----|
| `NotificationService` | Accept requests | Facade |
| `ChannelRouter` | Choose channels | Policy |
| `TemplateRenderer` | Safe render | Injection/XSS care for HTML |
| `Dispatcher` | Execute sends | Orchestration |
| `DeliveryRepository` | Status | Audit |

## Interfaces

| Port | Why |
|------|-----|
| `NotificationChannel` | `send(RenderedMessage)` |
| `PreferenceStore` | Opt-out |
| `RetryScheduler` | Delayed redo |
| `IdGenerator` / `Clock` | |

**Rejected:** `NotificationService` directly constructing `new TwilioClient()` — untestable vendor lock.

## Relationships

```text
NotificationService → TemplateRenderer → ChannelRouter
                   → PreferenceStore
                   → Dispatcher → List<NotificationChannel>
                   → DeliveryRepository
RetryScheduler → Dispatcher
```

## SOLID

- **OCP:** new channel = new adapter  
- **SRP:** render ≠ route ≠ send  
- **ISP:** channels only `send`  
- **DIP:** publishers depend on `NotificationService`, not SMS SDK  

## Design patterns

| Pattern | Where | Why |
|---------|-------|-----|
| Strategy / Adapter | Channels | Vendors |
| Template Method | Retry loop | Shared backoff |
| Observer-like | Domain events → notify | Decouple product |
| Decorator | Metrics/logging around channel | Cross-cutting |

## Thread safety

- Dispatcher may use thread pool for channel I/O  
- Delivery status updates atomic per `notificationId+channel`  
- Prefer queue between accept and send for backpressure (in-mem queue OK for LLD)  

## Error handling

| Failure | Behavior |
|---------|----------|
| Opt-out | Skip channel; not an error |
| Missing template param | Fail validation before send |
| Transient provider error | Retry with backoff |
| Permanent (bad number) | Mark FAILED; no infinite retry |
| Partial multi-channel | Per-channel status; don’t fail whole unnecessarily |

## Extensibility

| Change | Touch |
|--------|-------|
| WhatsApp channel | New `NotificationChannel` |
| Digest batching | New router/scheduler policy |
| Priority lane | Queue partition |

## Testing

- Fake channels record messages  
- Opt-out skips SMS  
- Retry invoked on transient stub  
- Template missing param throws before channel called  

## Trade-offs

| Choice | Why | Rejected |
|--------|-----|----------|
| Channel port list | Evolution | Enum switch forever |
| At-least-once + idempotency key | Reality of retries | Exactly-once myth in-process |
| Preferences before send | Compliance | Send-then-filter — wastes money/spam |
| Async queue | Latency isolation | Sync send in request thread — couples UX to Twilio |

**Staff phrasing:** “Publishers emit intent; routing and vendors are adapters; retries are explicit with dead-letter — not hidden loops.”
