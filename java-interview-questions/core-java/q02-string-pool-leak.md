# `String.intern()` under traffic

## Question

A service “optimizes” duplicate log tags by calling `String.intern()` on every request-derived string. After a traffic spike, latency rises and you see native/Metaspace or heap pressure depending on JDK. What’s the failure mode?

## Difficulty

Senior

## Expected answer

`intern()` puts strings into the JVM string pool (heap-backed in modern JDKs). Unbounded unique strings (user ids, URLs, trace payloads) permanently retain memory → GC pressure or OOM. Intern is for a **bounded** set of known symbols, not request data.

## Reasoning

Pool membership extends lifetime beyond the request. High cardinality × intern = retained set grows without bound. Profiling shows many unique `String` instances lingering.

## Follow-up

How would you dedupe strings safely for a metrics label set?

## Common mistake

“Intern always saves memory” — only for repeated constants with low cardinality.

## Principal-level discussion

Ban unbounded `intern()` in standards; use bounded caches with size/TTL for label dictionaries; prefer enums/constants for known tags. Add archunit/CI grep for `.intern(` in app code. Incident postmortem should treat this as a memory retention bug, not “GC tuning.”
