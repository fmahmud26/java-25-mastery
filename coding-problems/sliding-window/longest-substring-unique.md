# Longest Substring Without Repeating Characters

## Problem

Given string `s`, return length of longest substring with all unique characters.

Example: `"abcabcbb"` → `3` (`"abc"`).

## Constraints

- `0 ≤ n ≤ 5·10^4`  
- ASCII / extended ASCII often  

## Brute force

All substrings, check uniqueness with set → O(n³) / O(n²).

## Optimized approach

Variable window `[left,right]`. Expand `right`; if duplicate, advance `left` past previous occurrence. Track max length. Map char → last index.

## Reasoning

Once a char repeats inside the window, any start ≤ previous index is invalid; jump `left`.

## Complexity

- Time: O(n)  
- Space: O(σ) alphabet size

## Java 25 solution

```java
import java.util.HashMap;

public final class LongestSubstringUnique {
    private LongestSubstringUnique() {}

    public static int lengthOfLongestSubstring(String s) {
        var last = new HashMap<Character, Integer>();
        int left = 0, best = 0;
        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            if (last.containsKey(c) && last.get(c) >= left) {
                left = last.get(c) + 1;
            }
            last.put(c, right);
            best = Math.max(best, right - left + 1);
        }
        return best;
    }
}
```

## Edge cases

- Empty string → 0  
- All unique / all same  
- `"abba"` (left must not move backward)

## Interview explanation

“Sliding window with last-seen indices; when a char repeats inside the window, move left past it.”

## Follow-up

At most k distinct chars; longest with replacement.
