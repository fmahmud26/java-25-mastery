# Systems — Principal / Staff LLD Catalog

Each file is a full design using the section template (requirements through trade-offs). Read for **decision rationale**, not diagrams alone.

| # | System | Hard problem |
|---|--------|----------------|
| 1 | [parking-lot](./parking-lot.md) | Concurrent spot assignment + pluggable pricing |
| 2 | [elevator](./elevator.md) | Per-car state machine + dispatch strategy |
| 3 | [atm](./atm.md) | Auth session + cash/bank failure ordering |
| 4 | [vending-machine](./vending-machine.md) | Money/stock under explicit states |
| 5 | [library](./library.md) | Title vs copy + reservations |
| 6 | [hotel](./hotel.md) | Date-range inventory + holds |
| 7 | [movie-booking](./movie-booking.md) | Seat hold → pay → confirm |
| 8 | [payment-system](./payment-system.md) | Idempotency + provider ports |
| 9 | [notification-system](./notification-system.md) | Channel adapters + retry |
| 10 | [logger](./logger.md) | Async backpressure + context capture |
| 11 | [cache](./cache.md) | Eviction + singleflight stampede control |
| 12 | [rate-limiter](./rate-limiter.md) | Algorithm strategy + fail-open/closed |

Practice order by theme: inventory (1,5,6,7) → state machines (2,3,4) → platforms (8,9) → libraries (10,11,12).
