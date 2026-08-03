# Valid Parentheses

## Problem

Given a string containing only `()[]{}`, return whether brackets are valid: correctly matched and nested.

Example: `"()[]{}"` → true; `"(]"` → false.

## Constraints

- `1 ≤ n ≤ 10^4`  
- Only the six bracket characters  

## Brute force

Repeatedly remove `"()"`, `"[]"`, `"{}"` until stuck — O(n²).

## Optimized approach

Stack of opening brackets. On close, top must match; end with empty stack.

## Reasoning

LIFO mirrors nesting. Wrong closer or leftover opener ⇒ invalid.

## Complexity

- Time: O(n)  
- Space: O(n)

## Java 25 solution

```java
import java.util.ArrayDeque;

public final class ValidParentheses {
    private ValidParentheses() {}

    public static boolean isValid(String s) {
        var stack = new ArrayDeque<Character>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(' || c == '[' || c == '{') {
                stack.push(c);
            } else {
                if (stack.isEmpty()) return false;
                char open = stack.pop();
                if (c == ')' && open != '(') return false;
                if (c == ']' && open != '[') return false;
                if (c == '}' && open != '{') return false;
            }
        }
        return stack.isEmpty();
    }
}
```

## Edge cases

- Empty (if allowed) → true  
- Only closers  
- Correct types wrong order `([)]`  

## Interview explanation

“Push opens; on close, pop and match. Empty stack at end means valid.”

## Follow-up

Minimum removes to make valid; generate all valid n-pairs (backtracking).
