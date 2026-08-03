# Movie Booking

**Assumption:** single cinema or chain booking service; seat map per show. Payment via port.

## Requirements

- Browse movies/shows; view seat map  
- Hold seats temporarily; pay; confirm booking  
- Prevent double-booking under concurrency  
- Pricing by seat category; taxes optional  
- Cancel per policy  

**Non-goals:** video streaming; multi-country tax engine deep dive.

## Use cases

1. List shows  
2. Get seat layout availability  
3. Create hold (selected seats)  
4. Confirm after payment  
5. Expire holds; cancel booking  

## Domain model

| Concept | Invariant |
|---------|-----------|
| `Show` | Movie + screen + start time |
| `Seat` | Identity in screen; category |
| `SeatAvailability` per show | FREE / HELD / SOLD |
| `Hold` | Expires at `ttl`; seats set exclusive |
| `Booking` | Confirmed seats + payment ref |

**Why hold exists:** payment is slow and failure-prone; selling without hold races; holding forever kills conversion.

## Classes

| Class | Responsibility | Why |
|-------|----------------|-----|
| `ShowRepository` | Catalog | Read |
| `SeatInventory` | Per-show seat states | Invariant owner |
| `HoldService` | Create/expire holds | TTL logic |
| `BookingService` | Confirm/cancel | Orchestration |
| `PricingPolicy` | Seat category → money | Variation |

## Interfaces

| Port | Why |
|------|-----|
| `PaymentGateway` | Charge with idempotency key |
| `Clock` / `Scheduler` | Expiry |
| `Notifier` | Tickets |
| `PricingPolicy` | Promos |

## Relationships

```text
BookingService → HoldService → SeatInventory
BookingService → PaymentGateway
BookingService → PricingPolicy, Notifier
Hold ──locks──► seats on Show
Booking ──from──► Hold after paid
```

## SOLID

- **SRP:** inventory CAS ≠ payment  
- **OCP:** pricing strategy  
- **DIP:** gateway/notifier  
- **ISP:** narrow payment `charge`/`refund`  

## Design patterns

| Pattern | Where | Why |
|---------|-------|-----|
| Strategy | Pricing | Categories/promos |
| State | Seat FREE/HELD/SOLD | Clear transitions |
| Facade | BookingService | API |
| Scheduler | Hold expiry | Resource reclaim |

## Thread safety

- Critical invariant: seat on show never SOLD twice  
- Implementation: per-show lock **or** atomic CAS per seat status `FREE→HELD`  
- Prefer **striping by showId** over global lock  
- Payment **outside** seat lock; hold TTL covers abandon  

## Error handling

| Failure | Behavior |
|---------|----------|
| Seat taken on hold | Partial failure policy: all-or-nothing hold preferred |
| Payment fail | Hold remains until TTL or explicit release |
| Payment success, confirm fail | Idempotent confirm / reconciliation job |
| Expired hold confirm | Reject; do not charge again (idempotency) |

## Extensibility

| Change | Touch |
|--------|-------|
| Group/social seating rules | Validator before hold |
| Partner inventory | Port on inventory |
| Food add-ons | Line items on booking |

## Testing

- Two users hold same seat → one wins  
- Expiry returns seats to FREE  
- Idempotent confirm with same payment key  
- All-or-nothing multi-seat hold  

## Trade-offs

| Choice | Why | Rejected |
|--------|-----|----------|
| Hold + TTL | Matches real ticketing | Book only after pay without hold — double-sell under lag |
| All-or-nothing multi-seat | UX for couples | Partial holds — complex UX |
| Show-level striping | Throughput | DB serializable global — simpler, less scale |

**Staff emphasis:** narrate **hold → pay → confirm** and **idempotency** — this is the senior bar for this prompt.
