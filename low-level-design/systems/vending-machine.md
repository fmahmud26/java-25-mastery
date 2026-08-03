# Vending Machine

**Assumption:** one machine process; keypad + coin/cash/card reader ports.

## Requirements

- Select product; accept money; dispense; return change  
- Inventory per slot; out-of-stock rejection  
- Valid state transitions only (can’t dispense before paid)  
- Concurrent: usually single consumer; still protect inventory if card auth async  

**Non-goals:** warehouse restocking ERP; dynamic surge pricing platform.

## Use cases

1. Insert money / start card auth  
2. Select item  
3. Dispense + change  
4. Cancel → refund inserted cash  
5. Admin restock / set price  

## Domain model

| Concept | Invariant |
|---------|-----------|
| `VendingState` | IDLE → ACCEPTING → SELECTION → DISPENSING → IDLE |
| `Slot` / `Product` | Quantity ≥ 0; price ≥ 0 |
| `CashRegister` | Balance of inserted money; change from available coins |
| `Transaction` | Atomic success or full cancel/refund path |

**Why State pattern:** vending bugs are almost always illegal sequencing.

## Classes

| Class | Responsibility | Why |
|-------|----------------|-----|
| `VendingMachine` | State holder + facade | API |
| `Inventory` | Slots | Stock |
| `CashRegister` | Inserted + change maker | Money math |
| `Catalog` | Code → product/price | Pricing display |

## Interfaces

| Port | Why |
|------|-----|
| `PaymentAcceptor` | Cash vs card differ |
| `Dispenser` | Hardware |
| `ChangeDispenser` | Coin hopper |
| `MachineState` implementations | Transition behavior |

## Relationships

```text
VendingMachine → current MachineState
VendingMachine → Inventory, CashRegister
States call back to machine for transitions
PaymentAcceptor → notifies machine of credit
```

## SOLID

- **OCP:** new payment method via `PaymentAcceptor`  
- **SRP:** change calculation ≠ inventory  
- **DIP:** hardware ports  

## Design patterns

| Pattern | Where | Why |
|---------|-------|-----|
| State | Session lifecycle | Illegal ops |
| Strategy | Change making | Coin set varies |
| Facade | Machine API | Simple UI layer |

## Thread safety

- Prefer **single-threaded event loop** (UI events)  
- If card payment callback async: queue events to same loop so inventory decrement isn’t racy  

## Error handling

| Failure | Behavior |
|---------|----------|
| Unknown code / OOS | Reject select; keep credit |
| Can’t make change | Abort sale; refund (policy: exact-change mode) |
| Dispense jam | Enter FAULT; alarm; no silent charge |
| Cancel | Refund inserted cash |

## Extensibility

| Change | Touch |
|--------|-------|
| Cashless only | New acceptor; cash paths unused |
| Multi-item cart | New state + inventory multi-decrement |
| Age-restricted items | Gate in selection state |

## Testing

- Full happy path with fake dispenser  
- Exact-change failure path  
- Double-press select doesn’t double-dispense  
- Restock increases quantity  

## Trade-offs

| Choice | Why | Rejected |
|--------|-----|----------|
| Explicit State objects | Clarity under interview | One class 500-line switch — OK small, rot-prone |
| Credit held in register until success | Clear refund | Decrement stock early — painful rollback |
| Single event loop | Removes races | Synchronized spaghetti |

**Staff phrasing:** “The product is a state machine; money and stock are inventories with reservation until dispense confirms.”
