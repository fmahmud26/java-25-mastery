# Elevator

**Assumption:** one building controller JVM; N elevators, M floors. Hardware I/O behind ports.

## Requirements

- Cabin moves between floors; serves hall calls and cabin requests  
- Doors open/close with safe transitions  
- Dispatch assigns hall calls to elevators  
- Concurrent button presses must not corrupt target sets  
- Dispatch policy replaceable (nearest, sector, VIP)  

**Non-goals:** multi-building cluster consensus; predictive ML dispatch as core.

## Use cases

1. Hall call (UP/DOWN at floor)  
2. Cabin floor request  
3. Step simulation / tick (move, open, close)  
4. Emergency stop / out-of-service  

## Domain model

| Concept | Invariant |
|---------|-----------|
| `ElevatorCar` | One floor at a time; state ∈ IDLE/MOVING/DOOR_OPEN/… |
| `Direction` | Movement direction consistent with committed targets while MOVING |
| `HallCall` / `CabinRequest` | Idempotent add to pending set |
| `Dispatcher` | Every active hall call eventually assigned (liveness goal) |

**Why explicit state:** illegal ops (move while DOOR_OPEN) are design bugs if modeled as free flags.

## Classes

| Class | Responsibility | Why |
|-------|----------------|-----|
| `ElevatorCar` | Local state + pending stops | Owns cabin invariant |
| `Door` | Open/close timing hooks | Isolate door safety rules |
| `ElevatorController` | Per-car command processing | Single-writer for that car |
| `BuildingElevatorSystem` | Facade + dispatcher wiring | Entry API |
| `HallCall` | Floor + direction | Value request |

## Interfaces

| Port | Why |
|------|-----|
| `DispatchStrategy` | Buildings tune assignment |
| `MotorPort` / `DoorPort` | Hardware or simulator |
| `Clock` / scheduler | Deterministic tests |
| `ElevatorEventListener` | Display panels, metrics |

**Rejected:** giant `if` in one `System.loop` choosing cars — untestable and unextensible.

## Relationships

```text
BuildingElevatorSystem → DispatchStrategy
BuildingElevatorSystem → List<ElevatorController>
ElevatorController → ElevatorCar → Door
ElevatorController → MotorPort
Hall panel → System.requestHallCall
```

## SOLID

- **SRP:** dispatcher assigns; car executes; door manages door  
- **OCP:** new dispatch strategy without editing cars  
- **LSP:** strategies must only assign feasible cars  
- **ISP:** `DoorPort` ≠ `MotorPort`  
- **DIP:** car depends on ports, not GPIO library  

## Design patterns

| Pattern | Where | Why |
|---------|-------|-----|
| State | Car modes | Transition-safe commands |
| Strategy | Dispatch | Pluggable policies |
| Command | Enqueued requests | Serialize car ops |
| Observer | Floor displays | Decouple UI |

## Thread safety

- **Shared:** hall call queue; each car’s stop set  
- **Approach:** **single-threaded event loop per car** (command queue) — eliminates most car races  
- Dispatcher may run on controller thread; hand off via car queues  
- **Why:** elevator control is naturally sequential per cabin; mimicking that beats fine-grained locks  

## Error handling

| Failure | Behavior |
|---------|----------|
| Request to out-of-service car | Reject / reassign |
| Motor fault mid-move | Transition FAULT; clear unsafe commits; alert |
| Duplicate hall call | Idempotent — already pending |
| Invalid floor | Validation error at API |

## Extensibility

| Change | Touch |
|--------|-------|
| Priority/VIP floors | New `DispatchStrategy` |
| Double-decker | New car type + strategy constraints |
| Destination control (keypad in lobby) | New request type + dispatcher |

## Testing

- Simulated motor: assert stop order for a fixed request set  
- Never open doors between floors  
- Dispatch unit tests with fake cars reporting floor/direction  
- Stress: burst hall calls don’t drop under queue capacity policy  

## Trade-offs

| Choice | Why | Rejected |
|--------|-----|----------|
| Per-car single writer | Simpler correctness | Synchronized methods everywhere |
| Strategy dispatch | Real product variance | Hardcoded nearest-only |
| Explicit state enum/sealed | Illegal transitions fail fast | Boolean soup |
| Simulation tick API | Interview-testable | Hidden threads only — harder to reason |

**Staff note:** optimality of SCAN/LOOK is secondary to **correct state machine + clear ownership of pending stops**.
