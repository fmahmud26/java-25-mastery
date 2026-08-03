# SQL injection

## Question

Why is string-concatenated SQL with user input dangerous, and what’s the fix?

## Difficulty

Junior

## Expected answer

Attackers inject SQL. Use `PreparedStatement` parameters / bind variables always. Validate allowlists for dynamic identifiers (sort columns).

## Common mistake

Manual escaping instead of parameters.

## Follow-up

Can ORM criteria APIs still be unsafe?
