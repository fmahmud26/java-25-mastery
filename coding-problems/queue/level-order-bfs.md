# Binary Tree Level Order Traversal

## Problem

Given binary tree root, return node values level by level (BFS).

Example: `[3,9,20,null,null,15,7]` → `[[3],[9,20],[15,7]]`.

## Constraints

- `0 ≤ nodes ≤ 2000`  
- Node values arbitrary ints  

## Brute force

DFS with depth lists — also fine; BFS is the natural queue pattern.

## Optimized approach

Queue starts with root. While not empty: drain current level size, collect values, enqueue children.

## Reasoning

FIFO ensures left-to-right; level size snapshot separates levels.

## Complexity

- Time: O(n)  
- Space: O(n) worst (wide level)

## Java 25 solution

```java
import java.util.*;

public final class LevelOrderBfs {
    public record TreeNode(int val, TreeNode left, TreeNode right) {
        public TreeNode(int val) { this(val, null, null); }
    }

    private LevelOrderBfs() {}

    public static List<List<Integer>> levelOrder(TreeNode root) {
        var result = new ArrayList<List<Integer>>();
        if (root == null) return result;
        var q = new ArrayDeque<TreeNode>();
        q.add(root);
        while (!q.isEmpty()) {
            int size = q.size();
            var level = new ArrayList<Integer>(size);
            for (int i = 0; i < size; i++) {
                TreeNode node = q.remove();
                level.add(node.val());
                if (node.left() != null) q.add(node.left());
                if (node.right() != null) q.add(node.right());
            }
            result.add(level);
        }
        return result;
    }
}
```

## Edge cases

- Empty tree  
- Skewed tree (one node per level)  
- Missing children  

## Interview explanation

“BFS with a queue; process `size` nodes per level so we group by depth.”

## Follow-up

Zigzag order; right side view; average per level.
