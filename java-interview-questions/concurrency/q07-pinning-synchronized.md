# Virtual threads and `synchronized` on Java 25

## Question

A Principal candidate says “never use synchronized with virtual threads because pinning.” How do you grade that answer on **Java 25**?

## Difficulty

Principal

## Expected answer

Nuanced: JEP 491 (Java 24+) makes `synchronized` no longer pin virtual threads the old way—advice must be version-aware. Still avoid holding locks while doing blocking I/O for design reasons; prefer fine-grained locking. Don’t recite Java 21 pinning lore as eternal truth. Verify against current JEP/release notes.

## Reasoning

Interview signal: currency of knowledge + mechanism, not slogans. Pinning from JNI/native frames can still matter; measure.

## Follow-up

What else can pin or stall carriers on Java 25? (JNI/FFM, local file I/O, class init — measure with JFR.)

## Common mistake

Copy-pasting 2023 Loom blog posts into a 2026 interview.

## Principal-level discussion

Maintain an internal “Loom notes” doc versioned by JDK; update paved paths on LTS upgrades; educate via experiments that show current behavior; focus standards on deadlines, pool bounds, and not locking around remote calls—regardless of pinning mythology.
