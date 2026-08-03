# Maximum Depth of Binary Tree

## Problem

Return maximum depth (root-to-leaf longest path length in nodes) of a binary tree.

Example: `[3,9,20,null,null,15,7]` → `3`.

## Constraints

- `0 ≤ n ≤ 10^4`  
- Balanced or skewed  

## Brute force

BFS counting levels — also O(n); DFS is simplest recursion.

## Optimized approach

`depth(null)=0`; `depth(node)=1+max(depth(left),depth(right))`.

## Reasoning

Height is defined recursively from children.

## Complexity

- Time: O(n)  
- Space: O(h) recursion stack

## Java 25 solution

```java
public final class MaxDepth {
    public record TreeNode(int val, TreeNode left, TreeNode right) {
        public TreeNode(int val) { this(val, null, null); }
    }

    private MaxDepth() {}

    public static int maxDepth(TreeNode root) {
        if (root == null) return 0;
        return 1 + Math.max(maxDepth(root.left()), maxDepth(root.right()));
    }
}
```

## Edge cases

- Empty → 0  
- Single node → 1  
- Skewed chain  

## Interview explanation

“DFS: depth is one plus the deeper child. Base case null is 0.”

## Follow-up

Iterative BFS/stack; min depth; diameter of tree.
