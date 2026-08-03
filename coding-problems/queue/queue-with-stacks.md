# Implement Queue Using Stacks

## Problem

Implement a FIFO queue with two stacks supporting `push`, `pop`, `peek`, `empty`.

## Constraints

- Calls valid (no pop/peek on empty) or handle explicitly  
- Amortized O(1) expected for operations  

## Brute force

On every `pop`, reverse entire input stack into output — can be O(n) each time if done naively every call without lazy transfer.

## Optimized approach

`in` stack for push. On pop/peek, if `out` empty, pour all from `in` to `out`; then pop/peek `out`.

## Reasoning

Reversing twice restores FIFO. Pouring only when `out` is empty amortizes to O(1).

## Complexity

- Time: amortized O(1) per op  
- Space: O(n)

## Java 25 solution

```java
import java.util.ArrayDeque;
import java.util.Deque;

public final class QueueWithStacks {
    private final Deque<Integer> in = new ArrayDeque<>();
    private final Deque<Integer> out = new ArrayDeque<>();

    public void push(int x) {
        in.push(x);
    }

    public int pop() {
        move();
        return out.pop();
    }

    public int peek() {
        move();
        return out.peek();
    }

    public boolean empty() {
        return in.isEmpty() && out.isEmpty();
    }

    private void move() {
        if (out.isEmpty()) {
            while (!in.isEmpty()) {
                out.push(in.pop());
            }
        }
    }
}
```

## Edge cases

- Pop after interleaved push  
- Empty checks  
- Single element  

## Interview explanation

“Two stacks: push to in; lazily flip to out for pop/peek so FIFO order is preserved.”

## Follow-up

Stack using queues; complexity proof of amortization.
