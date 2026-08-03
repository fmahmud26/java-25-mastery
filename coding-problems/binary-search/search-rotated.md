# Search in Rotated Sorted Array

## Problem

Array was sorted ascending then rotated at unknown pivot. Search `target` in O(log n); return index or `-1`. Assume distinct values.

Example: `[4,5,6,7,0,1,2], target=0` → `4`.

## Constraints

- Distinct integers  
- `1 ≤ n ≤ 5000` typical  
- Exactly one rotation offset (including 0)

## Brute force

Linear scan O(n).

## Optimized approach

At mid, one half is sorted. If target in that half’s range, search there; else the other half.

## Reasoning

Rotation preserves sortedness in at least one half each step — still discard half.

## Complexity

- Time: O(log n)  
- Space: O(1)

## Java 25 solution

```java
public final class SearchRotated {
    private SearchRotated() {}

    public static int search(int[] nums, int target) {
        int lo = 0, hi = nums.length - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (nums[mid] == target) return mid;
            if (nums[lo] <= nums[mid]) { // left sorted
                if (nums[lo] <= target && target < nums[mid]) hi = mid - 1;
                else lo = mid + 1;
            } else { // right sorted
                if (nums[mid] < target && target <= nums[hi]) lo = mid + 1;
                else hi = mid - 1;
            }
        }
        return -1;
    }
}
```

## Edge cases

- No rotation  
- Target is pivot  
- Two elements  
- Duplicates → harder (not this problem)

## Interview explanation

“Still binary search: decide which side is sorted and whether target lies in that range.”

## Follow-up

With duplicates; find minimum in rotated array; rotated + target count.
