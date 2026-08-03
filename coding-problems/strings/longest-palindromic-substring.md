# Longest Palindromic Substring

## Problem

Return the longest palindromic **substring** of `s` (contiguous). Any longest is fine if ties.

Example: `"babad"` → `"bab"` or `"aba"`.

## Constraints

- `1 ≤ n ≤ 1000` (expand O(n²) OK); larger n needs Manacher  

## Brute force

Every substring, check palindrome → O(n³) or O(n²) with care.

## Optimized approach

**Expand around center:** for each center (char or between chars), expand while mirrors match; track best.

## Reasoning

A palindrome mirrors around a center. n char centers + n-1 gap centers cover odd/even lengths.

## Complexity

- Time: O(n²)  
- Space: O(1) extra

## Java 25 solution

```java
public final class LongestPalindromicSubstring {
    private LongestPalindromicSubstring() {}

    public static String longestPalindrome(String s) {
        int start = 0, end = 0;
        for (int i = 0; i < s.length(); i++) {
            int len = Math.max(expand(s, i, i), expand(s, i, i + 1));
            if (len > end - start) {
                start = i - (len - 1) / 2;
                end = i + len / 2;
            }
        }
        return s.substring(start, end + 1);
    }

    private static int expand(String s, int l, int r) {
        while (l >= 0 && r < s.length() && s.charAt(l) == s.charAt(r)) {
            l--;
            r++;
        }
        return r - l - 1;
    }
}
```

## Edge cases

- Single char  
- All same chars  
- Even-length palindrome `"abba"`  

## Interview explanation

“I’ll expand around each center for odd/even palindromes and keep the longest — O(n²).”

## Follow-up

Count palindromic substrings; Manacher O(n); DP table approach.
