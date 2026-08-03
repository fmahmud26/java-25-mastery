# Heap pollution from unchecked conversion

## Question

A library method returns `List` raw; callers cast to `List<String>` and later see `ClassCastException` when reading. Explain heap pollution and prevention.

## Difficulty

Senior

## Expected answer

Unchecked conversion lets non-`String` elements enter a `List` typed as `List<String>` at compile time. Erasure means the JVM can’t enforce element types until a cast at use. Prevent with generics everywhere, `@SafeVarargs` carefully, avoid raw types, enable `-Xlint:unchecked`.

## Reasoning

Generics are compile-time; raw types punch a hole. Pollution is latent until consumption.

## Follow-up

What does `StackWalker`/`ClassCastException` stack tell you in this bug?

## Common mistake

`@SuppressWarnings("unchecked")` without bounding the risk.

## Principal-level discussion

Fail CI on raw types in new code; quarantine legacy with adapters that copy into typed collections. Treat pollution CVE-adjacent when untrusted data enters typed structures.
