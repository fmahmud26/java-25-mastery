# Library

**Assumption:** single library system process (one or few branches). Fine calculation local; notifications via port.

## Requirements

- Catalog of titles and physical copies (or digital licenses)  
- Member borrow / return / renew with limits  
- Reservations when all copies lent  
- Fines for overdue; block borrow when policy says so  
- Concurrent checkout of last copy must be safe  

**Non-goals:** full ILS network; publisher rights management.

## Use cases

1. Search catalog  
2. Checkout copy to member  
3. Return / renew  
4. Place / fulfill reservation  
5. Assess fines; pay fine (port)  

## Domain model

| Concept | Invariant |
|---------|-----------|
| `Title` vs `Copy` | Borrow operates on `Copy`, not abstract title |
| `Loan` | Open loan references one copy + member; due date set |
| `Member` | Active; max concurrent loans; not blocked |
| `Reservation` | Queue per title; fulfillment assigns a returned copy |

**Why Title≠Copy:** waitlists and inventory diverge; designing only “Book” collapses reality.

## Classes

| Class | Responsibility | Why |
|-------|----------------|-----|
| `CatalogService` | Search | Read path |
| `LendingService` | Checkout/return/renew | Use-case owner |
| `Copy` | AVAILABLE/LOANED/RESERVED/LOST | State |
| `LoanPolicy` / `FinePolicy` | Limits & money | Vary by member type |
| `ReservationQueue` | FIFO per title | Fairness |

## Interfaces

| Port | Why |
|------|-----|
| `LoanPolicy` | Student vs faculty rules |
| `FinePolicy` | Grace days, caps |
| `Notifier` | Due soon / available |
| `Clock` | Due dates |
| `CopyRepository` / `MemberRepository` | Persistence boundary |

## Relationships

```text
LendingService → LoanPolicy, FinePolicy, Clock
LendingService → CopyRepo, LoanRepo, ReservationQueue
Copy ──loan──► Loan ──to──► Member
Title 1──* Copy; Title 1──* Reservation
```

## SOLID

- **SRP:** lending ≠ fine math ≠ notify  
- **OCP:** new member tier via policy  
- **DIP:** notifier port  

## Design patterns

| Pattern | Where | Why |
|---------|-------|-----|
| Strategy | Loan/fine policies | Tier rules |
| State | Copy status | Valid transitions |
| Observer/port | Notifications | Side effects out of core |
| Facade | `LibraryService` | API |

## Thread safety

- **Shared:** copy status, reservation head  
- Checkout: transactional `AVAILABLE → LOANED` on copy (optimistic version or row lock)  
- Last copy + reservation: define policy — either block open shelf or auto-hold for next reservation  

## Error handling

| Failure | Behavior |
|---------|----------|
| Member at loan limit | `LoanLimitExceeded` |
| Copy not available | Fail; offer reservation |
| Renew past max / with waitlist | Policy reject |
| Return of unknown loan | Error; don’t silently create |

## Extensibility

| Change | Touch |
|--------|-------|
| Digital loans | New `Loanable` type + policy |
| Inter-branch transfer | New process + copy location field |
| Short-loan “reserve desk” | New copy state + policy |

## Testing

- Concurrent checkout same copy → one wins  
- Fine calc with fixed clock  
- Reservation fulfill on return assigns correctly  
- Blocked member cannot checkout  

## Trade-offs

| Choice | Why | Rejected |
|--------|-----|----------|
| Title/Copy split | Correct inventory | Single Book entity — reservation bugs |
| Policy strategies | Academic libraries vary | Hardcoded limits in service |
| Soft reservation on return | Fairness | Ignore queue — simpler unfair |

**Staff note:** call out **copy identity** and **reservation vs walk-in** conflict explicitly — interviewers listen for that.
