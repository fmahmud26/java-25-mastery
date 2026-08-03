# Fibonacci with Memoization

## Problem

Compute F(n) where F(0)=0, F(1)=1, F(n)=F(n-1)+F(n-2). Focus on recursive structure + memo (teaching vehicle; iterative is production default).

## Constraints

- `0 ≤ n ≤ 40` without memo is slow; with memo fine much higher until overflow  
- Prefer long for larger n  

## Brute force

Naive recursion → exponential O(φ^n) duplicate work.

## Optimized approach

Memo map/array: `f(n)` computed once. Or bottom-up DP / two rolling variables.

## Reasoning

Overlap: F(n-1) and F(n-2) share a huge subtree. Cache turns tree into DAG.

## Complexity

- Time: O(n) with memo  
- Space: O(n)

## Java 25 solution

```java
import java.util.HashMap;
import java.util.Map;

public final class FibonacciMemo {
    private FibonacciMemo() {}

    public static long fib(int n) {
        return fib(n, new HashMap<>());
    }

    private static long fib(int n, Map<Integer, Long> memo) {
        if (n <= 1) return n;
        Long cached = memo.get(n);
        if (cached != null) return cached;
        long value = fib(n - 1, memo) + fib(n - 2, memo);
        memo.put(n, value);
        return value;
    }
}
```

## Edge cases

- `n = 0`, `n = 1`  
- Integer overflow for large n (use `long` / BigInteger)

## Interview explanation

“Naive fib recomputes the same subproblems. Memoization stores each F(i) once — O(n).”

## Follow-up

Bottom-up O(1) space; matrix exponentiation O(log n); when recursion vs iteration in interviews.
