# Contract Testing

Prove **provider and consumer agree** on an API without full e2e.

## Mental Model

```text
Consumer tests publish expectations (or pact)
Provider verifies against those contracts in CI
```

Tools: Pact, Spring Cloud Contract, OpenAPI-driven checks, schema registries (Avro/JSON Schema) for events.

## When

Many teams, independently deployed services, breaking changes expensive.

## Example

Order service (consumer) expects `POST /charge` → `201` + `{transactionId}`.  
Payment service (provider) runs contract verification in pipeline.

## Trade-offs

| + | − |
|---|---|
| Catch breaking API early | Contract maintenance |
| Faster than full e2e | Won’t catch deep business bugs |

Not a substitute for domain unit tests or DB integration tests.

### Related

[integration-testing.md](./integration-testing.md) · [service-examples.md](./service-examples.md)
