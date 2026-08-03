# Climbing Stairs

## Problem

You can climb 1 or 2 steps. How many distinct ways to reach the top of `n` stairs?

Example: `n=3` → `3` (`1+1+1`, `1+2`, `2+1`).

## Constraints

- `1 ≤ n ≤ 45` (int fits)

## Brute force

Recurse `ways(n)=ways(n-1)+ways(n-2)` without memo → exponential.

## Optimized approach

`dp[i] = dp[i-1] + dp[i-2]`; roll two variables.

## Reasoning

Last step is 1 or 2 from a valid smaller climb — Fibonacci structure.

## Complexity

- Time: O(n)  
- Space: O(1)

## Java 25 solution

```java
public final class ClimbStairs {
    private ClimbStairs() {}

    public static int climbStairs(int n) {
        if (n <= 2) return n;
        int a = 1, b = 2;
        for (int i = 3; i <= n; i++) {
            int c = a + b;
            a = b;
            b = c;
        }
        return b;
    }
}
```

## Edge cases

- `n = 1`, `n = 2`  

## Interview explanation

“Define ways to reach i from i-1 or i-2; that’s Fibonacci computed bottom-up.”

## Follow-up

k-step climbs; min cost climbing stairs.
