# Reverse Words in a String

## Problem

Given a string `s`, reverse the **order of words**. Words are sequences of non-space chars. Remove leading/trailing spaces; collapse multiple spaces to one.

Example: `"  hello   world  "` → `"world hello"`.

## Constraints

- `1 ≤ s.length ≤ 10^4`  
- Printable ASCII; at least one word in typical prompts  

## Brute force

`trim`, `split("\\s+")`, reverse list, `String.join` — fine for interviews if allowed.

## Optimized approach

Same idea with explicit scan, or reverse whole string then reverse each word (in-place style on char array).

## Reasoning

Word order reverse ≠ character reverse. Normalize whitespace as part of the contract.

## Complexity

- Time: O(n)  
- Space: O(n) for new string (immutable Java `String`)

## Java 25 solution

```java
public final class ReverseWords {
    private ReverseWords() {}

    public static String reverseWords(String s) {
        String[] parts = s.trim().split("\\s+");
        var sb = new StringBuilder();
        for (int i = parts.length - 1; i >= 0; i--) {
            sb.append(parts[i]);
            if (i > 0) sb.append(' ');
        }
        return sb.toString();
    }
}
```

## Edge cases

- Multiple spaces  
- Leading/trailing spaces  
- Single word  

## Interview explanation

“Normalize spaces, split into words, append from the end. O(n) time.”

## Follow-up

In-place on `char[]` with O(1) extra (language-dependent). Unicode whitespace?
