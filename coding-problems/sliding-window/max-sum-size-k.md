# Maximum Sum of Subarray of Size K

## Problem

Given `int[] nums` and positive `k`, return maximum sum of any contiguous subarray of length exactly `k`.

Example: `nums=[2,1,5,1,3,2], k=3` → `9` (`[5,1,3]`).

## Constraints

- `1 ≤ k ≤ n ≤ 10^5`  
- May include negatives (still fixed window)

## Brute force

Sum every window of size k → O(n·k).

## Optimized approach

Compute first window sum; slide: add `nums[i]`, remove `nums[i-k]`; track max.

## Reasoning

Adjacent windows of size k differ by one enter and one leave — O(1) update.

## Complexity

- Time: O(n)  
- Space: O(1)

## Java 25 solution

```java
public final class MaxSumSizeK {
    private MaxSumSizeK() {}

    public static int maxSum(int[] nums, int k) {
        if (k > nums.length) throw new IllegalArgumentException("k");
        int sum = 0;
        for (int i = 0; i < k; i++) sum += nums[i];
        int best = sum;
        for (int i = k; i < nums.length; i++) {
            sum += nums[i] - nums[i - k];
            best = Math.max(best, sum);
        }
        return best;
    }
}
```

## Edge cases

- `k == n`  
- Negatives in window  
- `k == 1` → max element  

## Interview explanation

“Fixed-size sliding window: maintain running sum while sliding by one.”

## Follow-up

Variable window (smallest sum ≥ target). Deque for max in window.
