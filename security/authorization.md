# Authorization

## Problem

After authentication: *what is this principal allowed to do?*

## Mental Model

```text
Principal + action + resource → allow / deny (default deny)
```

## Models

| Model | Idea |
|-------|------|
| RBAC | Roles → permissions |
| ABAC | Attributes / policies (time, risk, resource owner) |
| ACL | Per-resource entries |
| ReBAC | Relationship-based (owner, member) |

```java
// conceptual
if (!authz.can(principal, "order:refund", orderId)) {
    throw new AccessDeniedException();
}
```

## Production Scenario

**Order service:** customer may read own orders; agent may refund within limit; service account may update status — different principals, same API.

## Failures

- IDOR: authorize action but not **resource ownership** (`/orders/123` of another user)  
- Trusting client-sent roles in JWT without server policy  
- Missing checks on admin/debug endpoints  
- “Authenticated ⇒ authorized”  

## Trade-offs

Central policy engine vs annotations at controllers — consistency vs locality. Coarse roles vs fine-grained ABAC complexity.

## Design Rule

Authorize on **every** sensitive path (API, messaging consumer, batch job) — not only the UI.

### Related

[authentication.md](./authentication.md) · [security-failures.md](./security-failures.md) · [scenarios.md](./scenarios.md)
