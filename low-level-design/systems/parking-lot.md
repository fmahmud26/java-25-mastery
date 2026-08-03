# Parking Lot

**Assumption:** single JVM garage process. Multi-city SaaS is out of scope (would become distributed inventory).

## Requirements

- Multiple floors; typed spots (compact, large, EV, …)  
- Vehicle entry assigns a compatible free spot and issues a ticket  
- Exit computes fee from policy, releases spot  
- Concurrent entries must not double-assign a spot  
- Pricing and allocation rules change by garage without rewriting core flow  

**Non-goals:** PSP checkout UI, ANPR cameras as first-class, cross-garage reservations.

## Use cases

1. **Park** — vehicle arrives → allocate spot → occupy → emit ticket  
2. **Exit** — present ticket → compute fee → close ticket → free spot  
3. **Query availability** — free counts by spot type / floor  
4. **Admin** — reconfigure pricing hours; mark spot out of service  

## Domain model

| Concept | Invariant |
|---------|-----------|
| `Spot` | At most one active occupant; type fixed |
| `Ticket` | Open ticket references exactly one spot; closed tickets immutable |
| `Vehicle` | Declares required capability (size/EV) |
| `Money` / `Duration` | Value objects; no negative fee from policy bugs without explicit rule |

**Why tickets are first-class:** fee, audit, and dispute need entry time + spot identity separate from live occupancy.

## Classes

| Class | Responsibility | Why separate |
|-------|----------------|--------------|
| `ParkingLot` / `ParkingService` | Orchestrate park/exit | Facade for use cases — not fee math |
| `Floor` | Groups spots | Layout changes shouldn’t touch pricing |
| `Spot` | Occupancy state machine FREE/OCCUPIED/OOOS | Protects single-occupant invariant |
| `Ticket` (record) | Entry facts | Immutable after issue except status close |
| `SpotRegistry` | Index free spots by type | Fast allocation without scanning blindly |
| `Vehicle` (sealed) | Capability | Exhaustive handling of known kinds |

## Interfaces

| Port | Why |
|------|-----|
| `SpotAllocator` | Allocation heuristics differ (nearest, EV-prefer, fill bottom-up) |
| `PricingPolicy` | Hourly / flat / progressive / EV discount |
| `TicketStore` | In-memory vs DB later |
| `Clock` | Deterministic fees in tests |

**Rejected:** concrete `HourlyPricing` called directly from `ParkingService` — every city fork edits the service.

## Relationships

```text
ParkingService → SpotAllocator → SpotRegistry → Spot
ParkingService → PricingPolicy
ParkingService → TicketStore
ParkingService → Clock
Vehicle ──requires──► SpotType
Ticket ──references──► SpotId + Vehicle snapshot
```

Composition over inheritance for lot structure; polymorphism on `Vehicle` / policies.

## SOLID

- **SRP:** service orchestrates; policy prices; registry indexes  
- **OCP:** new pricing = new `PricingPolicy` impl  
- **LSP:** any allocator must never return an occupied/incompatible spot  
- **ISP:** `PricingPolicy` is fee-only — not allocation  
- **DIP:** service depends on ports, not Postgres/Stripe  

## Design patterns

| Pattern | Where | Why |
|---------|-------|-----|
| Strategy | Allocator, pricing | Pluggable algorithms |
| Facade | `ParkingService` | Clean interview entry API |
| Factory | Map input → `Vehicle` | Keep parsing out of domain |
| Sealed types | `Vehicle` | Closed set with clear extension process |

## Thread safety

- **Shared:** spot occupancy, free-index in registry  
- **Approach:** per-spot atomic state transition `FREE→OCCUPIED` (lock or CAS); registry update in same critical section as spot  
- **Why not lot-wide lock:** unrelated floors would serialize unnecessarily  
- **Do not lock across** fee display I/O or payment  

## Error handling

| Failure | Behavior |
|---------|----------|
| No compatible spot | `NoSpotAvailable` — no ticket created |
| Unknown / closed ticket on exit | `InvalidTicket` |
| Spot OOOS mid-stay | Operational exception path; don’t silently move car in LLD |
| Double exit | Idempotent close or explicit `TicketAlreadyClosed` |

Prefer domain exceptions (or `Result`) over nulls.

## Extensibility

| Change | Touch |
|--------|-------|
| New spot type | `SpotType` + allocator rule + maybe vehicle |
| Night pricing | New/composed `PricingPolicy` |
| Reservations | New `Reservation` aggregate + allocator that respects holds |

## Testing

- Fixed `Clock` for fee boundaries (exactly 1h vs 1h1s)  
- Fake registry with two spots — concurrent park → one success  
- Policy unit tests without service  
- Allocator never returns EV spot for non-EV when rule forbids  

## Trade-offs

| Choice | Why | Rejected |
|--------|-----|----------|
| Ticket immutable facts + status | Audit clarity | Mutating vehicle on spot only — loses history |
| Strategy for pricing | High change rate | Hardcoded fee in service |
| Per-spot locking | Throughput | Global lock — simpler but won’t scale floors |
| Single process | Matches LLD scope | Distributed lock service — HLD bleed |

**Sketch (Java 25):** sealed `Vehicle`; `record Ticket(...)`; `ParkingService.park/exit` delegating to allocator + policy + registry.
