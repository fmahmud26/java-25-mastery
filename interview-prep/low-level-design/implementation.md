# Low-Level Design — Implementation

Java 25 sketch style for whiteboard/coding LLD.

```java
public final class ParkingLot {
    private final Map<SpotType, Queue<Spot>> free;
    private final Map<TicketId, Spot> occupied;
    private final Pricing pricing;
    private final Object lock = new Object(); // or stripe by floor

    public ParkingLot(Pricing pricing, List<Spot> spots) { /* index free spots */ 
        this.pricing = pricing;
        this.free = index(spots);
        this.occupied = new HashMap<>();
    }

    public Ticket park(Vehicle v) {
        synchronized (lock) {
            var spot = pollCompatible(v.type());
            occupied.put(ticket.id(), spot);
            return ticket;
        }
    }
}
```

## Selection cheat sheet

| Need | Reach for |
|------|-----------|
| Policy variation | Strategy interface |
| Object creation complexity | Factory / builder |
| State machine (elevator) | Explicit states/sealed state |
| Unique IDs / tickets | Value types (`record`) |
| Notify subscribers | Observer / events (in-process) |
| Cross-cutting rules | Decorator carefully |

Related: [interfaces.md](../../low-level-design/concepts/interfaces.md), [composition.md](../../low-level-design/concepts/composition.md), [design-patterns.md](../../low-level-design/concepts/design-patterns.md).
