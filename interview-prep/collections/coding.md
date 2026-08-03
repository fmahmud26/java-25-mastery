# Collections — Coding

Patterns that show up constantly:

| Problem | Structure |
|---------|-----------|
| Two Sum | `HashMap` value→index |
| Group anagrams | `Map` signature→list |
| LRU cache | `LinkedHashMap` access-order / handcrafted |
| Top-K | `HashMap` + heap |
| Sliding window unique | `HashMap` last index / counts |

Practice: [../../coding-problems/hashing](../../coding-problems/hashing/), [two-pointers](../../coding-problems/two-pointers/).

```java
// Two Sum — coding interview staple
int[] twoSum(int[] nums, int target) {
    var index = new HashMap<Integer, Integer>();
    for (int i = 0; i < nums.length; i++) {
        Integer j = index.get(target - nums[i]);
        if (j != null) return new int[]{j, i};
        index.put(nums[i], i);
    }
    throw new IllegalArgumentException();
}
```

**Talk track:** brute force O(n²) → hash trade space for O(n) time.
