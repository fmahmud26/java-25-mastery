# Unsafe publication of a config object

## Question

Thread A constructs a `Config` (multi-field), assigns to a static `config = c` without volatile/synchronization. Thread B reads `config` and sees partially initialized fields. Why?

## Difficulty

Mid

## Expected answer

No happens-before between publishing writes and reader’s reads. Safe publication: volatile field, synchronized, final fields + freeze, concurrent structures, or assigning only after fully init with proper sync.

## Common mistake

“It works on my machine” under x86 friendliness.

## Follow-up

How do `final` fields help safe construction?
