# Merge K Sorted Lists

## Problem

Merge `k` sorted linked lists into one sorted list; return head.

Example: `[[1,4,5],[1,3,4],[2,6]]` → `1→1→2→3→4→4→5→6`.

## Constraints

- Total nodes `N` up to 10^4  
- Lists already sorted ascending  

## Brute force

Merge lists one by one → O(kN); or collect all values, sort, rebuild → O(N log N).

## Optimized approach

Min-heap of current heads (by value). Pop smallest, append, push its next if any.

## Reasoning

Always need the global minimum among k frontiers — heap provides it in O(log k).

## Complexity

- Time: O(N log k)  
- Space: O(k) heap

## Java 25 solution

```java
import java.util.PriorityQueue;

public final class MergeKLists {
    public static final class ListNode {
        int val;
        ListNode next;
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    private MergeKLists() {}

    public static ListNode mergeKLists(ListNode[] lists) {
        var heap = new PriorityQueue<ListNode>((a, b) -> Integer.compare(a.val, b.val));
        for (ListNode node : lists) {
            if (node != null) heap.offer(node);
        }
        ListNode dummy = new ListNode(0);
        ListNode tail = dummy;
        while (!heap.isEmpty()) {
            ListNode node = heap.poll();
            tail.next = node;
            tail = node;
            if (node.next != null) heap.offer(node.next);
        }
        return dummy.next;
    }
}
```

## Edge cases

- Empty array / all null lists  
- One list  
- Uneven lengths  

## Interview explanation

“Put each list head in a min-heap; repeatedly take the smallest and push its successor.”

## Follow-up

Divide-and-conquer pairwise merge; compare complexity to heap.
