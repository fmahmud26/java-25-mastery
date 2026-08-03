# Optional on public APIs

## Question

A teammate changes a public REST DTO / library method to return `Optional<User>` everywhere “to avoid null.” When is that wrong?

## Difficulty

Mid

## Expected answer

`Optional` is for return types that may be absent in **in-process** APIs—not fields, not parameters, not collections elements, not bare persistence entities for JSON. Overuse adds allocation and awkward serialization. Prefer empty collections, clear nullability annotations, or domain absence types at boundaries.

## Common mistake

`Optional` as field in JPA entities or as method parameter.

## Follow-up

How do you express “user not found” in a REST API instead?
