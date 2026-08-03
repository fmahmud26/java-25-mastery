# Two Sum II (Sorted)

## Problem

`numbers` is sorted ascending. Return 1-indexed indices of two numbers summing to `target`. Exactly one solution; constant extra space preferred.

Example: `[2,7,11,15], target=9` → `[1,2]`.

## Constraints

- Sorted non-decreasing  
- Exactly one answer  
- Prefer O(1) extra space  

## Brute force

Nested loops or binary search for each complement → O(n²) / O(n log n).

## Optimized approach

`left=0`, `right=n-1`. If sum too small, `left++`; too large, `right--`; equal → done.

## Reasoning

Array sorted ⇒ moving left increases sum, moving right decreases. Each step eliminates an impossible side.

## Complexity

- Time: O(n)  
- Space: O(1)

## Java 25 solution

```java
public final class TwoSumSorted {
    private TwoSumSorted() {}

    /** @return 1-based indices */
    public static int[] twoSum(int[] numbers, int target) {
        int left = 0, right = numbers.length - 1;
        while (left < right) {
            int sum = numbers[left] + numbers[right];
            if (sum == target) {
                return new int[]{left + 1, right + 1};
            }
            if (sum < target) left++;
            else right--;
        }
        throw new IllegalArgumentException("no solution");
    }
}
```

## Edge cases

- Negatives in sorted array  
- Pair at extremes  
- Duplicates  

## Interview explanation

“Because it’s sorted, two pointers from both ends adjust the sum in O(n) without a hash map.”

## Follow-up

3Sum / 4Sum builds on this. Unsorted → hashing (Two Sum I).
