# Fat interface across two teams

## Question

`PlatformCustomerService` has 40 methods. Growth team depends on 3; Identity on 10; every change recompiles everyone and creates release coupling. What’s the OOP/API fix at Staff level?

## Difficulty

Staff

## Expected answer

Interface segregation: split ports (`CustomerReader`, `CustomerVerifier`, `MarketingPreferences`). Publish small APIs; keep façade only if needed for legacy. Ownership follows interfaces.

## Reasoning

Fat interfaces create false coupling and political merge contention. ISP is an org boundary tool.

## Follow-up

How do you migrate without a flag day?

## Common mistake

“One microservice” but still one 40-method RPC stub.

## Principal-level discussion

Treat API surface as a product: version, deprecate, measure callers. ADR for split; codeowners per port; prevent re-accumulation via lint on interface method count / review checklist.
