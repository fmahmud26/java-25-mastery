# Hotel

**Assumption:** single property (or small group) booking engine in one service. Channel managers (OTA) are ports.

## Requirements

- Room types and physical rooms (or type-level inventory)  
- Search availability for date range  
- Book / cancel / modify with policies  
- Overbooking policy explicit (allow N% or forbid)  
- Concurrent booking same room/date must not double-sell beyond policy  

**Non-goals:** full PMS housekeeping UI; global hotel chain HLD.

## Use cases

1. Search rooms by dates, guests, type  
2. Quote price  
3. Create booking (hold → confirm)  
4. Cancel / no-show  
5. Check-in / check-out (optional LLD stretch)  

## Domain model

| Concept | Invariant |
|---------|-----------|
| `RoomType` | Capacity, amenities |
| `Room` (optional) | Concrete unit if assigning at book vs at check-in |
| `StayInterval` | `[start, end)` no inverted dates |
| `Booking` | Holds inventory allotment; status PENDING/CONFIRMED/CANCELLED |
| `RatePlan` / `PricingPolicy` | Nightly rates, weekend, promo |

**Why interval algebra matters:** overlapping bookings on same inventory key are the core invariant.

## Classes

| Class | Responsibility | Why |
|-------|----------------|-----|
| `InventoryService` | Allotment by type×date | Hot path |
| `BookingService` | Orchestrate hold/confirm/cancel | Use cases |
| `PricingPolicy` | Quote | Vary often |
| `CancellationPolicy` | Refund rules | Legal variance |
| `Booking` | Aggregate root | Consistency boundary |

## Interfaces

| Port | Why |
|------|-----|
| `PricingPolicy` | Seasonality |
| `CancellationPolicy` | Product rules |
| `PaymentGateway` | Deposit |
| `InventoryStore` | In-mem vs DB |
| `ChannelNotifier` | OTA push later |

## Relationships

```text
BookingService → InventoryService → InventoryStore
BookingService → PricingPolicy, CancellationPolicy, PaymentGateway
Booking ──covers──► RoomType + StayInterval + guest count
```

**Assignment timing decision:**  
- **Type-level book, assign room at check-in** — more flexible ops  
- **Room-level book** — simpler mental model, worse optimization  

**Why prefer type-level for Staff answer:** hotels optimize assignment; say it aloud.

## SOLID

- **SRP:** inventory ≠ pricing ≠ cancel refund  
- **OCP:** new rate plan as policy  
- **DIP:** payment port  

## Design patterns

| Pattern | Where | Why |
|---------|-------|-----|
| Strategy | Pricing, cancel, overbook | Policy knobs |
| Facade | BookingService | API |
| State | Booking status | Transitions |
| Reservation/hold | Temporary allotment | Payment race |

## Thread safety

- Inventory keys: `roomTypeId + date` counters  
- Hold: decrement available atomically; TTL expires hold  
- Confirm: convert hold → confirmed under same inventory rules  
- **No payment inside inventory lock**  

## Error handling

| Failure | Behavior |
|---------|----------|
| Insufficient allotment | `SoldOut` |
| Hold expired before pay | Restart search |
| Cancel outside window | Fee via policy; still free inventory |
| Double confirm | Idempotent by `bookingId` |

## Extensibility

| Change | Touch |
|--------|-------|
| Overbooking 5% | `OverbookingPolicy` |
| Connected rooms | Inventory constraints graph |
| Corporate negotiated rates | Pricing strategy |

## Testing

- Overlap cases on inventory calendar  
- Concurrent holds exhausting last room → one fails  
- Cancel restores allotment  
- Pricing weekend boundaries with fixed clock  

## Trade-offs

| Choice | Why | Rejected |
|--------|-----|----------|
| Type-level inventory | Real hotel ops | Always bind room id at booking |
| Soft hold + TTL | Payment latency | Block forever on abandon |
| Explicit overbook policy | Business reality | Pretend overbook never exists |

**Staff emphasis:** date-range inventory + hold/confirm is the design; UI is noise.
