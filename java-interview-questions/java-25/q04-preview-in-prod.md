# Preview features in production

## Question

Team wants `--enable-preview` in prod for Structured Concurrency on Java 25. Staff decision framework?

## Difficulty

Staff

## Expected answer

Preview = unstable across JDKs; allow only with explicit risk acceptance, limited blast radius, easy rollback, and plan to remove flag when final. Prefer stable alternatives (CF/VT) for core paths unless benefit is proven.

## Reasoning

Preview APIs can change/break upgrades—operational liability.

## Follow-up

How do you track preview removal?

## Common mistake

Silent preview in base images company-wide.

## Principal-level discussion

Written preview policy; architecture board exceptions; CI detects `--enable-preview`; migrate ASAP when finalized.
