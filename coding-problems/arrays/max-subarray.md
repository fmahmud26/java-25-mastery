# Maximum Subarray (Kadane)

## Problem

Given a non-empty `int[] nums`, return the largest sum of any **contiguous** subarray.

Example: `[-2,1,-3,4,-1,2,1,-5,4]` → `6` from `[4,-1,2,1]`.

## Constraints

- `1 ≤ n ≤ 10^5`  
- Values may be negative  
- At least one element (empty array out of scope)

## Brute force

For every `i..j`, compute sum → O(n²) with running sum, O(n³) if re-summing. Correct but too slow for n=1e5.

## Optimized approach

**Kadane:** track best sum ending at `i`. Either start new at `nums[i]` or extend previous. Track global max.

## Reasoning

Any optimal subarray ends at some index. Optimal ending at `i` is `nums[i]` plus optimal ending at `i-1` **only if** that helps. Discarding a negative prefix is never worse for a future end.

## Complexity

- Time: O(n)  
- Space: O(1)

## Java 25 solution

```java
public final class MaxSubarray {
    private MaxSubarray() {}

    public static int maxSubArray(int[] nums) {
        int bestEnd = nums[0];
        int answer = nums[0];
        for (int i = 1; i < nums.length; i++) {
            bestEnd = Math.max(nums[i], bestEnd + nums[i]);
            answer = Math.max(answer, bestEnd);
        }
        return answer;
    }
}
```

## Edge cases

- All negative → largest single element  
- Single element  
- Entire array optimal  

## Interview explanation

“I’ll use Kadane: at each index decide whether to extend or restart, keeping a global max. That’s O(n)/O(1). Brute every subarray is O(n²).”

## Follow-up

Return the **start/end indices** as well — keep indices when updating `bestEnd`/`answer`. Divide-and-conquer O(n log n) variant?
