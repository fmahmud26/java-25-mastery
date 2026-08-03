# Realistic Service Examples

## Order / Checkout service

| Layer | What to test |
|-------|----------------|
| **Unit** | `Order` state transitions; pricing/shipping strategies; validation |
| **Unit + stubs** | `CheckoutService` orchestration with stubbed `PaymentGateway` / `InventoryPort` |
| **Integration** | Persistence of order + lines; transaction rollback on stock failure (Postgres + Testcontainers) |
| **Contract** | Consumer contract for Payment `POST /charge` |
| **Smoke e2e** | One staging journey: create → pay → confirm |

## Inventory service

| Layer | What to test |
|-------|----------------|
| **Unit** | Reservation rules, sold-out policy |
| **Integration** | `UPDATE … WHERE qty >= ?` under two concurrent clients |
| **Contract** | Provider verification for `GET /stock/{sku}` schema |

## Payment adapter module

| Layer | What to test |
|-------|----------------|
| **Unit** | DTO mapping, idempotency key rules |
| **Integration** | HTTP client against WireMock/MockWebServer (status codes, timeouts) |
| **Avoid** | 100% mocked serialization with zero real JSON round-trip |

## Notification / outbox

| Layer | What to test |
|-------|----------------|
| **Unit** | Event payload builders |
| **Integration** | Outbox writer + publisher against real DB; consumer idempotency |

### Related

[test-strategy.md](./test-strategy.md) · [how-tests-fail-in-production.md](./how-tests-fail-in-production.md)
