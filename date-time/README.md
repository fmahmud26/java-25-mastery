# Date & Time (`java.time`) — Engineering Guide

Immutable, explicit types for calendars and instants. Prefer **`Instant` (UTC)** for “when it happened”; use local/zoned types for business calendars and display.

**Standards:** [DEEP_LEARNING_STANDARD.md](../DEEP_LEARNING_STANDARD.md) · [MASTER_INSTRUCTION.md](../MASTER_INSTRUCTION.md)

## Type chooser

```text
Date only (due date, DOB)           → LocalDate
Time only (store hours)             → LocalTime
Wall clock, no zone                 → LocalDateTime   ⚠️ not for distributed events
Absolute moment                     → Instant         ✅ default for events
Moment + offset in payload          → OffsetDateTime
Moment + region/DST rules           → ZonedDateTime
Elapsed time (timeouts, SLA)        → Duration
Calendar span (trials, ages)        → Period
```

## Study path

1. [localdate](./localdate.md) · [localtime](./localtime.md) · [localdatetime](./localdatetime.md)  
2. [instant](./instant.md) · [utc](./utc.md) · [offsetdatetime](./offsetdatetime.md) · [zoneddatetime](./zoneddatetime.md) · [time-zones](./time-zones.md)  
3. [duration](./duration.md) · [period](./period.md) · [datetimeformatter](./datetimeformatter.md)  
4. Incidents: [timezone-bugs-and-incidents](./timezone-bugs-and-incidents.md) · Drill: [interview.md](./interview.md)

## Systems in this folder

Payments · Orders · Scheduled jobs · Distributed services · Audit logs · International users

## Principal stance

Persist UTC instants. Attach `ZoneId` at the edge. Never trust `LocalDateTime.now()` as a global event time. Test DST transitions for the regions you serve.
