# Binary Search (Classic)

## Problem

Given sorted ascending `int[] nums` and `target`, return index or `-1` if absent.

Example: `[-1,0,3,5,9,12], target=9` → `4`.

## Constraints

- `1 ≤ n ≤ 10^4` (or larger)  
- Distinct or not — clarify for lower_bound variants  
- Sorted ascending  

## Brute force

Linear scan → O(n).

## Optimized approach

Maintain `[lo, hi]`. Compare mid; discard half that cannot contain target.

## Reasoning

Monotonic order guarantees one half is useless each step.

## Complexity

- Time: O(log n)  
- Space: O(1)

## Java 25 solution

```java
public final class ClassicSearch {
    private ClassicSearch() {}

    public static int search(int[] nums, int target) {
        int lo = 0, hi = nums.length - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (nums[mid] == target) return mid;
            if (nums[mid] < target) lo = mid + 1;
            else hi = mid - 1;
        }
        return -1;
    }
}
```

## Edge cases

- Empty (if allowed)  
- Target at ends  
- Absent between values  
- Overflow-safe mid (`lo + (hi-lo)/2`)

## Interview explanation

“Binary search halves the range each step using the sorted order — O(log n).”

## Follow-up

First/last occurrence; search insert position; binary search on answer.
