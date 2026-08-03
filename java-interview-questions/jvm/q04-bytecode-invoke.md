# invokevirtual vs invokestatic

## Question

Why might a hotspot prefer monomorphic `invokevirtual` call sites, and what breaks optimization?

## Difficulty

Mid

## Expected answer

JIT inlines monomorphic/bimorphic virtual calls. Megamorphic sites (many receivers) inhibit inlining → slower. Design stable hierarchies; avoid huge polymorphic megasites on hot paths.

## Common mistake

Micro-optimizing before profiles show megamorphic calls.

## Follow-up

What is a bimorphic inline cache?
