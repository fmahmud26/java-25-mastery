# StackOverflowError

Thread cannot push another **stack frame** — stack exhausted. Not a heap OOM.

## Mental Model

```text
Heap full  → OutOfMemoryError (heap …)
Stack full → StackOverflowError
```

## Technical Mechanism

```java
void recurse() {
    recurse(); // unbounded → SOE
}
```

| Cause | Fix |
|-------|-----|
| Infinite / cyclic recursion | Base case; break cycles |
| Very deep legitimate recursion | Iterative algorithm; sometimes larger `-Xss` |
| Huge frames | Shrink locals / avoid giant stack arrays |
| Tiny `-Xss` | Raise carefully; watch RSS × threads |

## JVM Internals

Platform thread stack sized by `-Xss` + OS. Virtual threads use growable continuation stacks — deep recursion can still fail; don’t assume infinite depth.

## Production Implications

SOE stack traces usually name the recursive methods. Fix design first. Raising `-Xss` for all threads multiplies RSS.

## Investigation

Thread dump / exception stack → recursion site. Not a heap dump problem.

## Interview / PE

SOE vs OOM? When is `-Xss` appropriate vs wrong?

### Related

[stack.md](./stack.md) · [outofmemoryerror.md](./outofmemoryerror.md)
