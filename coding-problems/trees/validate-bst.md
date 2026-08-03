# Validate Binary Search Tree

## Problem

Determine if a binary tree is a valid BST: for every node, left subtree values < node < right subtree values (strict), recursively.

Example: `[2,1,3]` → true; `[5,1,4,null,null,3,6]` → false.

## Constraints

- Node values may be `Integer.MIN/MAX` — use long bounds or nullables  
- `1 ≤ n ≤ 10^4` typical  

## Brute force

For each node, scan entire left/right subtrees for min/max — O(n²).

## Optimized approach

DFS with allowed `(low, high)` open interval; or inorder must be strictly increasing.

## Reasoning

BST constraint is global, not just vs immediate children — bounds thread the ancestor constraints.

## Complexity

- Time: O(n)  
- Space: O(h)

## Java 25 solution

```java
public final class ValidateBst {
    public record TreeNode(int val, TreeNode left, TreeNode right) {
        public TreeNode(int val) { this(val, null, null); }
    }

    private ValidateBst() {}

    public static boolean isValidBST(TreeNode root) {
        return dfs(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private static boolean dfs(TreeNode node, long low, long high) {
        if (node == null) return true;
        long v = node.val();
        if (v <= low || v >= high) return false;
        return dfs(node.left(), low, v) && dfs(node.right(), v, high);
    }
}
```

## Edge cases

- `Integer.MIN_VALUE` / `MAX_VALUE` as node values  
- Equal values (invalid under strict BST)  
- Right child smaller than ancestor  

## Interview explanation

“Pass down valid value ranges; each node must lie inside, then tighten for children. Or check inorder is sorted.”

## Follow-up

Recover BST with two swapped nodes; kth smallest in BST.
