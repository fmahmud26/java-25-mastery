# Product of Array Except Self

## Problem

Given `int[] nums`, return `answer` where `answer[i]` is product of all elements except `nums[i]`. Solve without division; O(n) time.

Example: `[1,2,3,4]` → `[24,12,8,6]`.

## Constraints

- `2 ≤ n ≤ 10^5`  
- Often: fit in 32-bit int for intermediate products in prompts — confirm  
- May contain zeros  

## Brute force

For each i, multiply all j≠i → O(n²).

## Optimized approach

`answer[i] = prefixProduct(i) * suffixProduct(i)`. Fill prefixes left→right, then multiply suffixes right→left in one extra pass (O(1) extra if allowed to use output array).

## Reasoning

Division fails with zeros and is forbidden. Separating left/right products avoids recounting.

## Complexity

- Time: O(n)  
- Space: O(1) extra beyond output (typical interview allowance)

## Java 25 solution

```java
public final class ProductExceptSelf {
    private ProductExceptSelf() {}

    public static int[] productExceptSelf(int[] nums) {
        int n = nums.length;
        int[] answer = new int[n];
        answer[0] = 1;
        for (int i = 1; i < n; i++) {
            answer[i] = answer[i - 1] * nums[i - 1];
        }
        int suffix = 1;
        for (int i = n - 1; i >= 0; i--) {
            answer[i] *= suffix;
            suffix *= nums[i];
        }
        return answer;
    }
}
```

## Edge cases

- One zero → only that index nonzero product  
- Two zeros → all zeros  
- Negatives  

## Interview explanation

“Prefix products into the output, then fold suffixes from the right. No division, O(n) time, O(1) extra space.”

## Follow-up

Must use O(1) extra — already did. Streaming / parallel prefix?
