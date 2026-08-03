# Production Scenarios

## 1) Customer login (authentication)

Password + MFA → session cookie (Secure, HttpOnly). Rate-limit. Password Argon2/bcrypt. Failure: credential stuffing without throttling.

## 2) Refund API (authorization)

Authenticated agent still needs `refund` permission and amount limit; order must belong to their tenant. Failure: IDOR on `orderId`.

## 3) Service-to-service payment (TLS + mTLS)

Order → Payment over HTTPS; optional client cert. TrustStore has bank CA; KeyStore holds client identity. Failure: expired client cert outage.

## 4) Field encryption at rest

PII columns encrypted with envelope keys from KMS. App decrypts for authorized reads only. Failure: key in Git; plaintext in logs.

## 5) Webhook verification (signatures)

Verify PSP signature headers with shared secret/HMAC before trusting payload. Failure: process unsigned webhooks.

## 6) Secret rotation

Dual-valid API keys during rotation window; metrics on auth failures; revoke old. Failure: single key cutover downtime.

### Related

[security-failures.md](./security-failures.md) · [principal-decisions.md](./principal-decisions.md)
