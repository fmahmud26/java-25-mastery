# equals & Hash-Based Collections

Membership and keys depend on the **equals/hashCode contract**.

## Contract

- Reflexive, symmetric, transitive, consistent; `x.equals(null)` → false.  
- If `a.equals(b)` then `a.hashCode() == b.hashCode()`.  
- Unequal objects may share a hash (collision).

## Production Failures

| Bug | Symptom |
|-----|---------|
| Mutable key fields change | “Lost” map entries |
| hashCode without equals (or vice versa) | Duplicates / failed lookups |
| Identity equals for value IDs | Duplicate “same” customers in Set |

## Modern Java

Prefer `record` keys/values for correct structural equality; or `Objects.equals`/`hash`.

## Interview

- Why override both?  
- What breaks if hashCode is constant?  
- **Principal:** entity equals by DB id — proxies, transient ids, HashMap hazards?

### Related

[hashcode.md](./hashcode.md) · [hashmap.md](./hashmap.md) · [hashset.md](./hashset.md)
