# House Robber

## Problem

Houses in a line with `nums[i]` money. Cannot rob adjacent houses. Maximize total.

Example: `[2,7,9,3,1]` → `12` (`2+9+1`).

## Constraints

- `1 ≤ n ≤ 100` or larger  
- Non-negative money typical  

## Brute force

Try all subsets with no two adjacent → exponential.

## Optimized approach

`dp[i] = max(dp[i-1], dp[i-2] + nums[i])` — skip or take house i. Roll two vars.

## Reasoning

Optimal decision at i depends only on whether i is robbed; adjacent constraint links i-1 and i-2.

## Complexity

- Time: O(n)  
- Space: O(1)

## Java 25 solution

```java
public final class HouseRobber {
    private HouseRobber() {}

    public static int rob(int[] nums) {
        int prev2 = 0; // best up to i-2
        int prev1 = 0; // best up to i-1
        for (int x : nums) {
            int cur = Math.max(prev1, prev2 + x);
            prev2 = prev1;
            prev1 = cur;
        }
        return prev1;
    }
}
```

## Edge cases

- One house  
- Two houses → max of them  
- Zeros  

## Interview explanation

“At each house: rob it plus best two back, or skip. Rolling variables give O(1) space.”

## Follow-up

House robber II (circle); tree variant (binary tree houses).
