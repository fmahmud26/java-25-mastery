# Coin Change

## Problem

Coins of given denominations (unlimited). Return **fewest** coins to make `amount`, or `-1` if impossible.

Example: `coins=[1,2,5], amount=11` → `3` (`5+5+1`).

## Constraints

- `1 ≤ coins.length ≤ 12` typical; amount up to `10^4`  
- Positive coin values  

## Brute force

Try all combinations / recursion without memo → exponential.

## Optimized approach

`dp[x] = min coins for amount x`. `dp[0]=0`; for each coin, update `dp[x]=min(dp[x], dp[x-coin]+1)`.

## Reasoning

Unbounded knapsack: optimal for `x` uses some coin `c` plus optimal for `x-c`.

## Complexity

- Time: O(amount · |coins|)  
- Space: O(amount)

## Java 25 solution

```java
import java.util.Arrays;

public final class CoinChange {
    private CoinChange() {}

    public static int coinChange(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1);
        dp[0] = 0;
        for (int x = 1; x <= amount; x++) {
            for (int c : coins) {
                if (c <= x) {
                    dp[x] = Math.min(dp[x], dp[x - c] + 1);
                }
            }
        }
        return dp[amount] > amount ? -1 : dp[amount];
    }
}
```

## Edge cases

- `amount = 0` → 0  
- Impossible amount  
- Coin larger than amount  

## Interview explanation

“dp[x] is min coins for x; try each coin as the last one. Sentinel > amount means impossible.”

## Follow-up

Number of combinations (order-insensitive); coin change II; greedy fails when?
