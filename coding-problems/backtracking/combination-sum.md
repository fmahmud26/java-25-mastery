# Combination Sum

## Problem

Given distinct positive candidates and target, return all unique combinations that sum to target. Same number may be reused unlimited times. Combinations are unique (order irrelevant).

Example: `candidates=[2,3,6,7], target=7` → `[[2,2,3],[7]]`.

## Constraints

- Positive candidates  
- Target positive  
- Output size can be large — prune early  

## Brute force

Unbounded nested search without pruning — slow and duplicates.

## Optimized approach

DFS from index `start`: try `candidates[i]`, recurse with `remain - candidates[i]` and same `i` (reuse); then advance `i`. Sort optional for prune when `candidates[i] > remain`.

## Reasoning

`start` index prevents permutations of the same multiset. Reusing `i` allows unlimited count.

## Complexity

- Time: exponential in target / min candidate (output-sensitive)  
- Space: O(target/min) depth

## Java 25 solution

```java
import java.util.*;

public final class CombinationSum {
    private CombinationSum() {}

    public static List<List<Integer>> combinationSum(int[] candidates, int target) {
        Arrays.sort(candidates);
        var result = new ArrayList<List<Integer>>();
        dfs(candidates, 0, target, new ArrayList<>(), result);
        return result;
    }

    private static void dfs(int[] cand, int start, int remain, List<Integer> path, List<List<Integer>> result) {
        if (remain == 0) {
            result.add(new ArrayList<>(path));
            return;
        }
        for (int i = start; i < cand.length; i++) {
            if (cand[i] > remain) break;
            path.add(cand[i]);
            dfs(cand, i, remain - cand[i], path, result); // reuse i
            path.removeLast();
        }
    }
}
```

## Edge cases

- No combination → empty list  
- Exact single candidate  
- Many small numbers  

## Interview explanation

“Backtrack with a start index so we don’t reshuffle the same combo; stay on the same index to allow reuse.”

## Follow-up

Combination sum II (each number once, duplicates in input); coin change count (DP).
