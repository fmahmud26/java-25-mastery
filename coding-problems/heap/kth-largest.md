# Kth Largest Element in an Array

## Problem

Return the k-th largest element in `nums` (not distinct — i.e. order statistic).

Example: `[3,2,1,5,6,4], k=2` → `5`.

## Constraints

- `1 ≤ k ≤ n ≤ 10^5`  
- Prefer better than full sort if asked  

## Brute force

Sort descending, take index k-1 → O(n log n).

## Optimized approach

Min-heap of size k: push all; if size > k, poll. Root is k-th largest. (Or Quickselect average O(n).)

## Reasoning

Heap keeps the k largest; the smallest among them is the k-th largest.

## Complexity

- Time: O(n log k) heap; average O(n) Quickselect  
- Space: O(k)

## Java 25 solution

```java
import java.util.PriorityQueue;

public final class KthLargest {
    private KthLargest() {}

    public static int findKthLargest(int[] nums, int k) {
        var minHeap = new PriorityQueue<Integer>();
        for (int x : nums) {
            minHeap.offer(x);
            if (minHeap.size() > k) {
                minHeap.poll();
            }
        }
        return minHeap.peek();
    }
}
```

## Edge cases

- `k == 1` / `k == n`  
- Duplicates  
- Negatives  

## Interview explanation

“Maintain a min-heap of size k; after processing all, the top is the k-th largest — O(n log k).”

## Follow-up

Quickselect; streaming k-th; kth smallest (max-heap).
