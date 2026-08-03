# ConcurrentModificationException while iterating

## Question

Iterating an `ArrayList` while another part of the code `remove`s elements throws `ConcurrentModificationException`. Is the list “broken”? What are safe approaches?

## Difficulty

Junior

## Expected answer

Fail-fast iterators detect structural modification via `modCount`. Use `Iterator.remove`, `removeIf`, copy-on-write carefully, or synchronize all access. CME is a bug detector, not corruption by itself.

## Common mistake

Catching CME and ignoring it.

## Follow-up

Does `ConcurrentHashMap` iterators throw CME the same way?
