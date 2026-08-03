# Interview Playbook for Coding Problems

Use this script for every problem in this folder.

## 1. Clarify (1–2 min)

- Inputs/outputs, constraints, edge cases  
- Sorted? Distinct? Mutate in place? Return indices or values?  
- Restate the problem in one sentence  

## 2. Brute force first

- State the naive idea and complexity  
- Shows you can solve it; buys time to optimize  

## 3. Optimize with a named pattern

| Clue | Reach for |
|------|-----------|
| Complement / frequency / group | Hashing |
| Sorted pair / opposite ends | Two pointers |
| Contiguous + constraint | Sliding window |
| Nested matching / next greater | Stack |
| Level-by-level / FIFO | Queue / BFS |
| Rewire nodes / cycle | Linked list pointers |
| Sorted or monotonic answer | Binary search |
| Hierarchy | Tree DFS/BFS |
| Dependencies / grid components | Graph |
| Top-K / merge streams | Heap |
| All configurations | Recursion / backtracking |
| Min/max/ways with overlap | DP |

## 4. Speak while coding

- Name invariants (“window always unique”, “stack decreasing”)  
- Complexity before finishing  
- Dry-run one example + one edge case  

## 5. After code

- Edge cases from the file  
- Answer the **Follow-up** aloud  
- Trade-offs: time vs space, mutate vs copy  

## Anti-patterns

- Jumping to code with no approach  
- Memorized code without reasoning  
- Ignoring constraints (O(n²) on n=1e5)  
- Silent coding for 20 minutes  

## Cadence

Pattern → 1–2 problems deeply (full template) → same pattern cold next day → mix patterns weekly.
