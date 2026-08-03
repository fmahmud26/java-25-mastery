# Scoped values for request context

## Question

Why prefer Scoped Values over ThreadLocal for request context on modern JDKs?

## Difficulty

Senior

## Expected answer

Scoped Values are immutable, designed for structured nested use with VT-friendly propagation patterns, avoiding inheritance/leak footguns of ThreadLocal. Confirm API final status on your JDK; migrate deliberately.

## Reasoning

Context should have clear lifetime; ThreadLocal cleanup bugs amplify with VT.

## Follow-up

Can you rebind scoped values?

## Common mistake

Assuming drop-in replacement without API study.

## Principal-level discussion

Standardize context type in the framework layer; ban ad-hoc ThreadLocals in app code; document migration for MDC.
