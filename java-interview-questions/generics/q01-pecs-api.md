# PECS when designing copy APIs

## Question

You design `copy(src, dest)`. Signatures fight you: sometimes `List<Integer>` won’t pass where `List<Number>` is needed. How does PECS help?

## Difficulty

Mid

## Expected answer

Producer extends (`List<? extends T>` as source), Consumer super (`List<? super T>` as dest): `copy(List<? extends T> src, List<? super T> dest)`. Wildcard capture expresses variance Java generics lack on concrete `List<T>`.

## Common mistake

Using raw `List` to “make it compile.”

## Follow-up

Why can’t you `add` to `List<? extends T>`?
