# Course Schedule

## Problem

`numCourses` courses labeled `0..numCourses-1`. `prerequisites[i] = [a,b]` means take `b` before `a`. Return whether you can finish all courses (no cycle).

Example: `2, [[1,0]]` → true; `2, [[1,0],[0,1]]` → false.

## Constraints

- `1 ≤ numCourses ≤ 2000`  
- Directed edges  

## Brute force

Try all orderings — exponential.

## Optimized approach

Build adjacency + indegrees; Kahn’s BFS topo sort. If processed count < n → cycle. Or DFS with visiting colors (white/gray/black).

## Reasoning

Valid schedule ⇔ DAG. Topo sort fails iff cycle.

## Complexity

- Time: O(V+E)  
- Space: O(V+E)

## Java 25 solution

```java
import java.util.*;

public final class CourseSchedule {
    private CourseSchedule() {}

    public static boolean canFinish(int numCourses, int[][] prerequisites) {
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) adj.add(new ArrayList<>());
        int[] indeg = new int[numCourses];
        for (int[] e : prerequisites) {
            adj.get(e[1]).add(e[0]);
            indeg[e[0]]++;
        }
        var q = new ArrayDeque<Integer>();
        for (int i = 0; i < numCourses; i++) {
            if (indeg[i] == 0) q.add(i);
        }
        int taken = 0;
        while (!q.isEmpty()) {
            int u = q.remove();
            taken++;
            for (int v : adj.get(u)) {
                if (--indeg[v] == 0) q.add(v);
            }
        }
        return taken == numCourses;
    }
}
```

## Edge cases

- No prerequisites  
- Self-loop  
- Disconnected components  

## Interview explanation

“Model as a digraph; if topological sort can order all nodes, no cycle — courses are finishable.”

## Follow-up

Return any valid order; parallel semesters (levels); weighted longest path.
