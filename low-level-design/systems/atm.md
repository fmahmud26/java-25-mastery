# ATM

**Assumption:** one ATM process talking to bank services via ports. Cash cassette is local hardware.

## Requirements

- Card auth (PIN) with lockout policy  
- Session-scoped operations: balance, withdraw, deposit (optional), eject  
- Dispense exact cash using available denominations  
- Concurrent sessions: one per machine; bank APIs may be concurrent across ATMs  
- Fail safe: on uncertainty after dispense/debit, alarm + reconcile path  

**Non-goals:** full core-banking ledger design; EMV chip crypto deep dive.

## Use cases

1. Insert card → authenticate → create session  
2. Withdraw: authorize → dispense → confirm  
3. Balance inquiry  
4. Cancel / timeout → eject card, end session  
5. Admin: load cassettes; out-of-service  

## Domain model

| Concept | Invariant |
|---------|-----------|
| `Card` / `AccountRef` | PIN tries bounded; locked card cannot auth |
| `AtmSession` | At most one active; idle timeout |
| `CashInventory` | Denomination counts never negative |
| `Withdrawal` | Dispense iff bank auth succeeds **or** explicit compensating policy documented |

**Why session object:** groups authz, timeout, and operation sequencing (can’t withdraw twice overlapping).

## Classes

| Class | Responsibility | Why |
|-------|----------------|-----|
| `AtmMachine` | Facade / state of ATM READY/IN_SESSION/MAINTENANCE | Entry |
| `AtmSession` | Authenticated context | Lifecycle |
| `CashDispenser` | Mix denominations | Hard algorithm isolation |
| `Cassette` | Count per denom | Inventory |
| `PinAuthenticator` | Verify + lockout | Security policy elsewhere |

## Interfaces

| Port | Why |
|------|-----|
| `BankService` | Balance, authorize withdraw, confirm | Swap stubs/real |
| `CardReader` / `PinPad` / `DispenserHardware` | Device adapters |
| `Clock` | Timeouts |
| `AlarmService` | Ops alert on fault |

**Rejected:** ATM calling JDBC to bank DB — wrong trust boundary.

## Relationships

```text
AtmMachine → AtmSession (0..1)
AtmMachine → CashDispenser → Cassettes
AtmSession → BankService
AtmMachine → CardReader, PinPad
```

## SOLID

- **SRP:** dispenser ≠ bank auth ≠ session timeout  
- **OCP:** new withdrawal limit policy as strategy  
- **DIP:** hardware and bank behind ports  
- **ISP:** `BalanceQuery` vs `WithdrawalAuth` if needed  

## Design patterns

| Pattern | Where | Why |
|---------|-------|-----|
| State | ATM / session lifecycle | Illegal ops blocked |
| Strategy | Cash mixing (greedy vs DP) | Different cassettes |
| Facade | `AtmMachine` | User journey API |
| Template | Withdrawal steps | Fixed secure order |

## Thread safety

- Physical ATM: **one session**; UI thread + hardware callbacks serialized  
- `CashInventory` updates synchronized with dispense  
- Bank calls **outside** local cash lock after plan computed; define order carefully  

**Critical ordering (why):**

1. Compute mix under inventory lock; tentatively reserve  
2. Release lock; call bank authorize  
3. On success, dispense; on failure, release reservation  
4. If dispense fails after debit → alarm + `ReconciliationNeeded`  

## Error handling

| Failure | Behavior |
|---------|----------|
| Wrong PIN | Increment tries; lock card at threshold |
| Insufficient cash / can’t make amount | Fail before bank debit |
| Bank timeout after reserve | Release reserve; do not dispense |
| Partial dispense | Stop; alarm; do not pretend success |
| Session timeout | Eject; cancel pending |

## Extensibility

| Change | Touch |
|--------|-------|
| Deposit with cash acceptor | New use case + hardware port |
| Contactless | New auth strategy |
| Multi-currency cassettes | Inventory model + mixer |

## Testing

- Dispenser: exact mixes; impossible amounts  
- PIN lockout at N  
- Fake `BankService` timeout mid-withdraw → no dispense / reserve released  
- Session timeout with fixed clock  

## Trade-offs

| Choice | Why | Rejected |
|--------|-----|----------|
| Reserve then authorize then dispense | Minimize “cash out without debit” and reverse | Debit then dispense only — still need reverse; order must be explicit |
| Ports for bank/hardware | Testability + vendor swap | Monolith device drivers in domain |
| Greedy mixer default | Simple; works for standard denoms | Always DP — premature unless required |
| Single local session | Matches device | Multi-user ATM — nonsense physically |

**Staff emphasis:** speak to **two-phase failure** (money movement vs cash) — this is where senior signal shows.
