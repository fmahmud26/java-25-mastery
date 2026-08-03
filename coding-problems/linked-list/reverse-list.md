# Reverse Linked List

## Problem

Reverse a singly linked list; return new head.

Example: `1→2→3→4→5` → `5→4→3→2→1`.

## Constraints

- `0 ≤ n ≤ 5000`  
- Node values arbitrary  

## Brute force

Copy values to array, reverse, rebuild — O(n) space.

## Optimized approach

Iterative: `prev=null`, `curr=head`; save `next`, point `curr.next=prev`, advance.

## Reasoning

Each link must flip once; three pointers avoid losing the rest of the list.

## Complexity

- Time: O(n)  
- Space: O(1) iterative

## Java 25 solution

```java
public final class ReverseList {
    public static final class ListNode {
        int val;
        ListNode next;
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    private ReverseList() {}

    public static ListNode reverseList(ListNode head) {
        ListNode prev = null;
        ListNode curr = head;
        while (curr != null) {
            ListNode next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }
        return prev;
    }
}
```

## Edge cases

- Empty / single node  
- Two nodes  

## Interview explanation

“Iteratively rewire next pointers with prev/curr/next — O(1) space.”

## Follow-up

Recursive reverse; reverse between left/right; reverse k-group.
