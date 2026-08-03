# Number of Islands

## Problem

`grid` of `'1'` (land) and `'0'` (water). Island = 4-connected land. Return number of islands.

Example:  
`[["1","1","0"],["1","0","0"],["0","0","1"]]` → `2`.

## Constraints

- `m, n ≤ 300` typical  
- 4-directional connectivity  

## Brute force

Same flood idea; no worse asymptotic if careful — key is mark visited once.

## Optimized approach

Scan cells; on unvisited land, increment count and DFS/BFS mark entire component as visited (flip to `'0'` or visited matrix).

## Reasoning

Each connected component is one island; flood fill visits each land cell once.

## Complexity

- Time: O(m·n)  
- Space: O(m·n) worst recursion/queue

## Java 25 solution

```java
public final class NumberOfIslands {
    private NumberOfIslands() {}

    public static int numIslands(char[][] grid) {
        int count = 0;
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                if (grid[r][c] == '1') {
                    count++;
                    dfs(grid, r, c);
                }
            }
        }
        return count;
    }

    private static void dfs(char[][] grid, int r, int c) {
        if (r < 0 || c < 0 || r >= grid.length || c >= grid[0].length || grid[r][c] != '1') {
            return;
        }
        grid[r][c] = '0';
        dfs(grid, r + 1, c);
        dfs(grid, r - 1, c);
        dfs(grid, r, c + 1);
        dfs(grid, r, c - 1);
    }
}
```

## Edge cases

- All water / all land  
- Single cell  
- Diagonal touching (not connected under 4-dir)

## Interview explanation

“Treat the grid as a graph; each unvisited land starts a flood fill that marks one island.”

## Follow-up

Max island area; 8-connectivity; Union-Find; walls and gates multi-source BFS.
