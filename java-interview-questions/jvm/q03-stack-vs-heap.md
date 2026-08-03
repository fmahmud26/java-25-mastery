# Stack vs heap

## Question

Where do local primitives and object instances live? What causes `StackOverflowError` vs `OutOfMemoryError: heap`?

## Difficulty

Junior

## Expected answer

Locals/call frames on stack; objects on heap (references on stack). Deep recursion → SOE; too many/large objects → heap OOME.

## Common mistake

“All variables are on the heap.”

## Follow-up

Can escape analysis keep an object off-heap-of-record (scalar replace)?
