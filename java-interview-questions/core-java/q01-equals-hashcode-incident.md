# Broken `equals`/`hashCode` and disappearing map entries

## Question

Users report intermittent “settings lost.” You find a `HashMap<UserKey, Settings>` where `UserKey` is mutable and `hashCode` depends on a field that changes after `put`. What is going wrong, and how do you fix it?

## Difficulty

Mid

## Expected answer

After `put`, changing a field that participates in `hashCode` moves the logical bucket; `get` uses the new hash and misses the entry (or finds wrong collisions). Fix: immutable keys (records with all-final components), or don’t mutate key fields after insertion; ensure `equals`/`hashCode` contract consistency.

## Common mistake

Only checking `equals` and ignoring `hashCode`, or using identity (`==`) accidentally in custom keys.

## Follow-up

What happens if two keys are `equals` but have different `hashCode`?
