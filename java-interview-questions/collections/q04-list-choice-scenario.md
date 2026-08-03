# Choosing List implementation

## Question

You need a list of 10M timestamps for sequential scan and occasional append. Colleague suggests `LinkedList` “because inserts are O(1).” Your call?

## Difficulty

Mid

## Expected answer

Prefer `ArrayList` (contiguous, cache-friendly, O(1) index). `LinkedList` node allocation and pointer chasing kills throughput; “O(1) insert” ignores getting to the index. Rarely prefer LinkedList in modern Java.

## Common mistake

Big-O worship without constants/memory locality.

## Follow-up

When is `ArrayDeque` a better Queue than `LinkedList`?
