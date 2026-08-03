# Linked List Cycle

## Problem

Return whether a singly linked list contains a cycle.

## Constraints

- `0 ≤ n ≤ 10^4`  
- Do not modify structure (preferred)  
- O(1) space preferred  

## Brute force

HashSet of seen nodes → O(n) space.

## Optimized approach

**Floyd:** slow +1, fast +2. Meet ⇒ cycle; fast hits null ⇒ no cycle.

## Reasoning

In a cycle, faster pointer laps slower. Outside a cycle, fast reaches end.

## Complexity

- Time: O(n)  
- Space: O(1)

## Java 25 solution

```java
public final class HasCycle {
    public static final class ListNode {
        int val;
        ListNode next;
        ListNode(int val) { this.val = val; }
    }

    private HasCycle() {}

    public static boolean hasCycle(ListNode head) {
        ListNode slow = head;
        ListNode fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) return true;
        }
        return false;
    }
}
```

## Edge cases

- Empty / one node no self-loop  
- Self-loop  
- Cycle not at head  

## Interview explanation

“Floyd’s tortoise and hare: if they meet, there’s a cycle; O(1) space.”

## Follow-up

Find cycle entrance (reset one pointer to head after meeting). Cycle length.
