# Stack Frames

Detail companion to [stack.md](./stack.md): one **frame** = one method activation.

## Mental Model

```text
frame N     bar()     ← current
frame N-1   foo()
frame 0     main()
```

## Technical Mechanism

```java
void foo(int a) {
    int b = a + 1;
    String s = "x";   // ref in frame; String on heap
    bar(s);
}
```

Push on call, pop on return (or exception unwind). Locals that are references keep heap objects reachable for the frame’s lifetime.

## Production Notes

- Infinite recursion → [StackOverflowError](./stackoverflowerror.md)  
- Closed-over outer `this` in inner classes is a **heap** retention issue, not a stack issue  

### Related

[stack.md](./stack.md) · [object-retention.md](./object-retention.md)
