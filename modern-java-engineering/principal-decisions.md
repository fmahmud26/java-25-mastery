# Principal Engineer Decisions

## 1) Types over tribal knowledge

Money, OrderId, sealed payment results — illegal states unrepresentable where practical.

## 2) Boundaries are sacred

Validate & map at adapters; domain stays pure; don’t leak JPA/Stripe types upward.

## 3) Operability is a feature

Timeouts, logs, metrics, traces required for merge on new external calls.

## 4) API compatibility

Multi-team APIs versioned; changelogs; consumer contracts.

## 5) Error taxonomy

Document retryable vs terminal; never swallow on money paths.

## 6) Complexity budget

Reject frameworks/patterns that don’t pay rent in *this* codebase.

## Anti-decisions

- “Smart” mutable god objects  
- Optional parameters everywhere  
- Logging secrets  
- Copy-paste pricing “temporarily” across services  

### Related

[trade-offs.md](./trade-offs.md) · [observability.md](./observability.md) · [api-design.md](./api-design.md)
