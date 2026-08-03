# Streaming large JDBC result sets

## Question

Staff review: code does `jdbcTemplate.queryForList` loading 5M rows into a stream pipeline in heap. Design a safer approach.

## Difficulty

Staff

## Expected answer

Stream with cursor/fetch size (`setFetchSize`), hold connection only for cursor lifetime, process and write out incrementally, or push aggregation to SQL. Never materialize multi-million row lists. Mind transaction duration and DB cursor timeouts.

## Reasoning

Heap and GC collapse under huge materialization; DB connections held too long hurt pools.

## Follow-up

How do virtual threads change (or not) connection pool sizing here?

## Common mistake

`parallel()` over a JDBC-backed stream while sharing one `ResultSet`.

## Principal-level discussion

Paved path for export jobs: batch worker service, SQL-side filters, object storage spill, observability on fetch rates. Prohibit unbounded `queryForList` via code review checklist for data jobs.
