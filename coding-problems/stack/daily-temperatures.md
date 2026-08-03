# Daily Temperatures

## Problem

`temperatures[i]` is the temperature on day `i`. Return `answer[i]` = days until a warmer temperature; `0` if none.

Example: `[73,74,75,71,69,72,76,73]` → `[1,1,4,2,1,1,0,0]`.

## Constraints

- `1 ≤ n ≤ 10^5`  
- Temperatures in a bounded range (e.g. 30–100)  

## Brute force

For each day, scan forward for first warmer → O(n²).

## Optimized approach

**Monotonic decreasing stack** of indices. While current temp > stack top temp, pop and set wait = `i - popped`.

## Reasoning

Stack holds unresolved colder days. First warmer resolves them in amortized O(1) each.

## Complexity

- Time: O(n) amortized  
- Space: O(n)

## Java 25 solution

```java
import java.util.ArrayDeque;

public final class DailyTemperatures {
    private DailyTemperatures() {}

    public static int[] dailyTemperatures(int[] temperatures) {
        int n = temperatures.length;
        int[] answer = new int[n];
        var stack = new ArrayDeque<Integer>(); // indices, decreasing temps
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && temperatures[i] > temperatures[stack.peek()]) {
                int j = stack.pop();
                answer[j] = i - j;
            }
            stack.push(i);
        }
        return answer;
    }
}
```

## Edge cases

- Strictly decreasing → all zeros  
- Strictly increasing → all ones (except last)  
- Plateaus (equal not warmer)

## Interview explanation

“Monotonic stack of indices waiting for a warmer day; when found, fill distances.”

## Follow-up

Next greater element; circular array variant; previous smaller.
