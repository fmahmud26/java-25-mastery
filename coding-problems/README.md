# Coding Problems — Interview Pattern Prep

Pattern-first preparation. Each file is one **reusable technique**, not a random solution dump.

## How to practice

1. Read **Problem** + **Constraints** — clarify aloud (2 min).  
2. Attempt **brute force** yourself.  
3. Derive **optimized** with **reasoning** before peeking at code.  
4. Implement from memory, then compare **Java 25 solution**.  
5. Rehearse **Interview explanation** + answer **Follow-up**.

## Pattern map

| Pattern | Idea | Problems |
|---------|------|----------|
| [arrays](./arrays/) | Prefix/suffix, in-place, Kadane | max subarray, product except self |
| [strings](./strings/) | Scan, expand, parse | reverse words, longest palindromic substring |
| [hashing](./hashing/) | Value→index / frequency | two sum, group anagrams |
| [two-pointers](./two-pointers/) | Opposite or same direction | two sum sorted, container water |
| [sliding-window](./sliding-window/) | Expand/shrink contiguous range | longest unique substring, max sum size k |
| [stack](./stack/) | LIFO, monotonic | valid parentheses, daily temperatures |
| [queue](./queue/) | FIFO, BFS | queue with stacks, level order |
| [linked-list](./linked-list/) | Rewire, fast/slow | reverse, has cycle |
| [binary-search](./binary-search/) | Sorted / answer space | classic search, rotated array |
| [trees](./trees/) | DFS/BFS hierarchy | max depth, validate BST |
| [graphs](./graphs/) | DFS/BFS, topo | islands, course schedule |
| [heap](./heap/) | Top-K, merge | kth largest, merge k lists |
| [recursion](./recursion/) | Base + smaller instance | subsets, fibonacci memo |
| [backtracking](./backtracking/) | Choose → explore → undo | permutations, combination sum |
| [dynamic-programming](./dynamic-programming/) | Overlap + optimal substructure | climb stairs, coin change, house robber |

## Required problem template

Every problem file contains:

```text
Problem → Constraints → Brute force → Optimized approach
→ Reasoning → Complexity → Java 25 solution
→ Edge cases → Interview explanation → Follow-up
```

Speaking guide: [interview.md](./interview.md)

## Cross-links

| Need | Go to |
|------|--------|
| Answer spine | [../interview-prep/answer-framework.md](../interview-prep/answer-framework.md) |
| Coding track | [../interview-prep/tracks/coding.md](../interview-prep/tracks/coding.md) |
| Timed mock | [../interview-prep/formats/mock-interviews/coding-45.md](../interview-prep/formats/mock-interviews/coding-45.md) |
| Rapid-fire patterns | [../interview-prep/formats/rapid-fire/coding-patterns.md](../interview-prep/formats/rapid-fire/coding-patterns.md) |
