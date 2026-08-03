# Interview — Date & Time

Java 25 / `java.time`. Prefer **storage vs display** answers and incident stories.

---

## Core comparisons

### Instant vs LocalDateTime?

| Instant | LocalDateTime |
|---------|----------------|
| Absolute moment | Wall fields, no zone |
| Events, logs, APIs | Local templates / UI before zone |
| DST-safe | Ambiguous without zone |

### UTC vs local?

Persist UTC (`Instant`); show local with `ZoneId`. Never use server local as global truth.

### Duration vs Period?

Elapsed time vs calendar units. Timeouts → Duration; “+1 month” → Period/plusMonths.

### ZoneId vs ZoneOffset?

Region rules vs fixed offset. Scheduling → ZoneId; wire offset → OffsetDateTime → Instant.

### ZonedDateTime vs OffsetDateTime?

DST-aware region vs fixed offset label.

---

## Why avoid Date / Calendar?

Mutable, confusing (0-based months), weak zones, `SimpleDateFormat` not thread-safe. Use `java.time`; interop via `toInstant()`.

---

## Scenario questions

1. **Payments:** What type for `capturedAt`? Refund window — Duration or Period?  
2. **Orders:** Delivery promise “by Friday local” — model?  
3. **Scheduled jobs:** Daily 02:00 in `Europe/Berlin` across DST — how?  
4. **Distributed services:** How do nodes agree on expiry?  
5. **Audit logs:** Sort key? Display?  
6. **International users:** Store timezone how (IANA)?  

---

## Incident questions

Explain a DST double-fire. Explain LocalDateTime.now() cross-service bug. Why tzdb updates matter.

---

## Quick fire

| Q | A |
|---|---|
| Thread-safe format? | DateTimeFormatter |
| `LocalDate.now()` risk? | Default zone |
| Gap/overlap? | DST spring/fall |
| Clock injection? | Testability |
| Ban in review? | Date, Calendar, SDF, naked LDT events |

### Related

[README.md](./README.md) · [timezone-bugs-and-incidents.md](./timezone-bugs-and-incidents.md) · [utc.md](./utc.md) · [instant.md](./instant.md)
