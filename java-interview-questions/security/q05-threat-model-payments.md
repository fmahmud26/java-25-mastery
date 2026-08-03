# Threat modeling a payment service

## Question

As Principal, what threats do you insist are modeled before launch?

## Difficulty

Principal

## Expected answer

Authn/z, replay, double charge, insider access, webhook forgery, data at rest, dependency risk, logging PII/PAN, SSRF to PSP admin, supply chain. Require controls: idempotency, signature verify, least privilege, tokenization, audit ledger, rate limits.

## Reasoning

Payments combine money integrity + abuse + compliance.

## Follow-up

How do you verify webhook authenticity?

## Common mistake

Only OWASP Top 10 checkbox without money-specific flows.

## Principal-level discussion

Security architecture review gate; abuse cases in acceptance tests; continuous controls monitoring; clear ownership for keys/secrets.
