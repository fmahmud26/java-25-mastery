# try-with-resources for JDBC

## Question

Why prefer try-with-resources for `Connection`/`Statement`/`ResultSet` over closing in `finally`?

## Difficulty

Junior

## Expected answer

Try-with-resources guarantees `close()` on all declared resources in reverse order, handles suppressed exceptions correctly, and reduces leak bugs when early returns/exceptions happen.

## Common mistake

Closing `Connection` but leaving `ResultSet`/`Statement` open; or closing in wrong order manually.

## Follow-up

What happens to suppressed exceptions when both body and `close()` throw?
