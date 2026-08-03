# Subsets

## Problem

Given distinct integers `nums`, return all possible subsets (power set). Order of subsets flexible.

Example: `[1,2,3]` → `[[],[1],[2],[1,2],[3],[1,3],[2,3],[1,2,3]]`.

## Constraints

- Distinct elements  
- `n ≤ 10` typical (2^n output)

## Brute force

Bitmask `0..(1<<n)-1` — also O(n·2^n); recursion teaches the pattern.

## Optimized approach

At index `i`: either skip `nums[i]` or take it into the path; recurse; undo when taking (backtracking style).

## Reasoning

Each element independently in or out → 2^n subsets. Recursion encodes that choice tree.

## Complexity

- Time: O(n·2^n)  
- Space: O(n) recursion + output

## Java 25 solution

```java
import java.util.*;

public final class Subsets {
    private Subsets() {}

    public static List<List<Integer>> subsets(int[] nums) {
        var result = new ArrayList<List<Integer>>();
        dfs(nums, 0, new ArrayList<>(), result);
        return result;
    }

    private static void dfs(int[] nums, int i, List<Integer> path, List<List<Integer>> result) {
        if (i == nums.length) {
            result.add(new ArrayList<>(path));
            return;
        }
        dfs(nums, i + 1, path, result);          // exclude
        path.add(nums[i]);
        dfs(nums, i + 1, path, result);          // include
        path.removeLast();
    }
}
```

## Edge cases

- Empty input → `[[]]`  
- Single element  

## Interview explanation

“For each index, recurse with exclude and include; copy the path at the leaf. That’s the power set.”

## Follow-up

Subsets with duplicates (skip same values); combinations of size k.
