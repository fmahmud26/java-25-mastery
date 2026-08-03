# Retained heap climbs; RSS follows

## Question

Old gen after GC stays high; histogram shows millions of `byte[]`/`Session`. How do you find the GC root?

## Difficulty

Senior

## Expected answer

Heap dump → analyze retained size paths (Eclipse MAT/VisualVM) to GC roots (caches, statics, threads). Fix by bounding/evicting sessions; verify with after-dump.

## Reasoning

Rising post-GC heap ⇒ retention, not just allocation rate.

## Follow-up

Shallow vs retained size?

## Common mistake

Tuning GC first without dump analysis.

## Principal-level discussion

Require dump runbooks; PII handling for dumps; SLO on session store (Redis) instead of JVM heap sessions at scale.
