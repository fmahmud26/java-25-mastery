# Permutations

## Problem

Given distinct `nums`, return all permutations.

Example: `[1,2,3]` → all 6 orderings.

## Constraints

- Distinct integers  
- `n ≤ 6–8` typical (n! growth)

## Brute force

Generate all orderings with nested loops — doesn’t scale; recursion is the pattern.

## Optimized approach

Backtracking: build path; at each step pick unused element; recurse; unmark/remove.

## Reasoning

Permutations = arrangements of remaining items. Undo restores state for the next sibling choice.

## Complexity

- Time: O(n·n!)  
- Space: O(n) + output

## Java 25 solution

```java
import java.util.*;

public final class Permutations {
    private Permutations() {}

    public static List<List<Integer>> permute(int[] nums) {
        var result = new ArrayList<List<Integer>>();
        boolean[] used = new boolean[nums.length];
        dfs(nums, used, new ArrayList<>(), result);
        return result;
    }

    private static void dfs(int[] nums, boolean[] used, List<Integer> path, List<List<Integer>> result) {
        if (path.size() == nums.length) {
            result.add(new ArrayList<>(path));
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (used[i]) continue;
            used[i] = true;
            path.add(nums[i]);
            dfs(nums, used, path, result);
            path.removeLast();
            used[i] = false;
        }
    }
}
```

## Edge cases

- Single element  
- Two elements  

## Interview explanation

“Build a path by choosing an unused number, recurse, then undo — classic backtracking.”

## Follow-up

Permutations with duplicates; next permutation in-place.
