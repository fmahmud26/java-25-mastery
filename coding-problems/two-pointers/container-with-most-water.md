# Container With Most Water

## Problem

`height[i]` is a vertical line at x=`i`. Choose two lines that with the x-axis form a container holding the most water. Return max area.

Example: `[1,8,6,2,5,4,8,3,7]` → `49`.

## Constraints

- `2 ≤ n ≤ 10^5`  
- Heights ≥ 0  

## Brute force

Every pair `(i,j)` area = `min(h[i],h[j])*(j-i)` → O(n²).

## Optimized approach

Start `left`/`right` at ends. Area from current pair; move the **shorter** pointer inward (only way to possibly increase min height enough to beat width loss).

## Reasoning

Width starts max. The limiting height is the shorter line; keeping the taller and moving the shorter is the only candidate for improvement.

## Complexity

- Time: O(n)  
- Space: O(1)

## Java 25 solution

```java
public final class ContainerWithMostWater {
    private ContainerWithMostWater() {}

    public static int maxArea(int[] height) {
        int left = 0, right = height.length - 1, best = 0;
        while (left < right) {
            int h = Math.min(height[left], height[right]);
            best = Math.max(best, h * (right - left));
            if (height[left] < height[right]) left++;
            else right--;
        }
        return best;
    }
}
```

## Edge cases

- Strictly increasing / decreasing  
- All equal heights  
- Many zeros  

## Interview explanation

“Two pointers from the outside; always advance the shorter side while tracking max area — O(n).”

## Follow-up

Trapping rain water (different pattern: stack or two-pass). Prove correctness briefly.
