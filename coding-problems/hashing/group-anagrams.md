# Group Anagrams

## Problem

Group strings that are anagrams of each other.

Example: `["eat","tea","tan","ate","nat","bat"]` → `[["bat"],["nat","tan"],["ate","eat","tea"]]` (order flexible).

## Constraints

- `1 ≤ strs.length ≤ 10^4`  
- `0 ≤ strs[i].length ≤ 100`  
- Lowercase English often assumed  

## Brute force

For each string, compare to all groups by sorting both → roughly O(n² · k log k).

## Optimized approach

Map **canonical key** → list. Key = sorted chars, or 26-count signature string.

## Reasoning

Anagrams share the same multiset of characters → same key.

## Complexity

- Time: O(n · k log k) with sort key; O(n · k) with count key  
- Space: O(n · k)

## Java 25 solution

```java
import java.util.*;

public final class GroupAnagrams {
    private GroupAnagrams() {}

    public static List<List<String>> groupAnagrams(String[] strs) {
        var groups = new HashMap<String, List<String>>();
        for (String s : strs) {
            char[] chars = s.toCharArray();
            Arrays.sort(chars);
            String key = new String(chars);
            groups.computeIfAbsent(key, _ -> new ArrayList<>()).add(s);
        }
        return new ArrayList<>(groups.values());
    }
}
```

## Edge cases

- Empty string  
- Singletons  
- All anagrams of one word  

## Interview explanation

“Sort each word as a key into a hash map of lists. Anagrams collide on the same key.”

## Follow-up

Count-array key for O(n·k). Unicode? Case sensitivity?
