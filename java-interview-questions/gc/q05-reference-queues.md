# Soft vs weak references for caches

## Question

A cache uses `SoftReference` expecting “memory-sensitive” behavior, but latency becomes unpredictable under pressure. Trade-offs?

## Difficulty

Mid

## Expected answer

Soft refs clear under memory pressure with unclear timing—can cause stampede/latency cliffs. Prefer explicit sized caches (Caffeine) with eviction policies. Weak refs for canonicalizing maps where GC identity matters.

## Common mistake

SoftReference as a free cache product.

## Follow-up

What is a `ReferenceQueue` used for?
