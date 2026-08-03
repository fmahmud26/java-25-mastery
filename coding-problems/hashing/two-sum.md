# Two Sum

## Problem

Given `int[] nums` and target `t`, return indices of **two distinct** elements that sum to `t`. Assume exactly one solution.

Example: `nums=[2,7,11,15], t=9` → `[0,1]`.

## Constraints

- `2 ≤ n ≤ 10^4` (or larger — map still fine)  
- Exactly one valid answer (classic statement)  
- Cannot reuse same index  

## Brute force

Nested loops try every pair → O(n²) time, O(1) space.

## Optimized approach

One pass hash map `value → index`. For `nums[i]`, look up `t - nums[i]` before inserting `nums[i]`.

## Reasoning

Complement check turns “find pair” into “have I seen the partner?” Trading O(n) space for O(n) time.

## Complexity

- Time: O(n) expected  
- Space: O(n)

## Java 25 solution

```java
import java.util.HashMap;

public final class TwoSum {
    private TwoSum() {}

    public static int[] twoSum(int[] nums, int target) {
        var indexByValue = new HashMap<Integer, Integer>();
        for (int i = 0; i < nums.length; i++) {
            int need = target - nums[i];
            Integer j = indexByValue.get(need);
            if (j != null) {
                return new int[]{j, i};
            }
            indexByValue.put(nums[i], i);
        }
        throw new IllegalArgumentException("no solution");
    }
}
```

## Edge cases

- Negatives and zeros  
- Duplicate values (`[3,3], t=6`)  
- Partner at ends  

## Interview explanation

“Brute is all pairs. Optimized: while scanning, store seen values; if complement exists, return indices. O(n) time and space.”

## Follow-up

Return all pairs? Sorted array → two pointers. Multiple solutions / no solution contracts?
