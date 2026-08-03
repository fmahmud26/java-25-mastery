# Timezone Bugs & Production Incidents

Real failure patterns — use in design reviews and postmortems.

---

## Incident 1 — Payment reconciliation skew

**Symptom:** Finance says captures “missing” for an hour each night.  
**Cause:** One service logged `LocalDateTime.now()` (JVM `Asia/Dhaka`); warehouse assumed UTC.  
**Fix:** Persist `Instant`; display only at edges.  
**Prevent:** Schema type review; ban `LocalDateTime.now()` in payment domain.

---

## Incident 2 — Double charge window on DST fall-back

**Symptom:** Scheduler fires twice for “1:30am” local job.  
**Cause:** Cron in local zone during overlap; job not idempotent.  
**Fix:** Idempotency keys; schedule on Instant/UTC or use zone-aware library with overlap policy.  
**Prevent:** Test Europe/London transition weekends.

---

## Incident 3 — Order “today” cutoff wrong for EU users

**Symptom:** Customers can’t place orders evening local time.  
**Cause:** `LocalDate.now()` on UTC server for “business day” in Paris.  
**Fix:** `LocalDate.now(ZoneId.of("Europe/Paris"))` (or market zone).  

---

## Incident 4 — Audit log sort chaos

**Symptom:** Events appear out of order in UI.  
**Cause:** Sorted strings of mixed offsets / local datetimes.  
**Fix:** Sort by Instant; format later.

---

## Incident 5 — Distributed lease expiry

**Symptom:** Lock released early on some nodes.  
**Cause:** Compared `LocalDateTime` across hosts with drifted/default zones.  
**Fix:** `Instant` + `Duration`; NTP discipline.

---

## Incident 6 — International invoice PDF

**Symptom:** Invoice date off by one for US users.  
**Cause:** Converted Instant→LocalDate using server zone.  
**Fix:** Customer billing `ZoneId`.

---

## Checklist for new services

1. Event columns = Instant / timestamptz  
2. Inject `Clock`  
3. User/market `ZoneId` in profile  
4. Jobs: define zone + idempotency  
5. Tests for DST gap/overlap if you schedule locally  
6. Formatter thread-safety (`DateTimeFormatter` only)

### Related

[utc.md](./utc.md) · [zoneddatetime.md](./zoneddatetime.md) · [interview.md](./interview.md)
