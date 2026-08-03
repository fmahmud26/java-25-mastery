# DateTimeFormatter

Immutable, **thread-safe** formatting/parsing (unlike `SimpleDateFormat`).

## Mental Model

```text
pattern / ISO / locale → format(temporal) / parse
Always know: Instant vs local vs zoned
```

## Java 25 Examples

```java
DateTimeFormatter iso = DateTimeFormatter.ISO_INSTANT;
String wire = iso.format(Instant.now(clock));

DateTimeFormatter ui = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm z")
        .withLocale(Locale.UK)
        .withZone(ZoneId.of("Europe/London"));
String display = ui.format(Instant.now(clock));

LocalDate d = LocalDate.parse("03/08/2026", DateTimeFormatter.ofPattern("dd/MM/uuuu"));
```

```java
// Strict payment file dates
DateTimeFormatter yyyymmdd = new DateTimeFormatterBuilder()
        .appendPattern("uuuuMMdd")
        .toFormatter()
        .withResolverStyle(ResolverStyle.STRICT);
```

## Production Systems

| System | Use |
|--------|-----|
| Audit logs | ISO_INSTANT / ISO_OFFSET_DATE_TIME |
| Payments | Clear PSP format patterns; reject lenient parse |
| International users | Locale + user ZoneId |
| Orders | Invoice dates in customer locale |

## Common Bugs

| Bug | Fix |
|-----|-----|
| `SimpleDateFormat` shared static | Use DateTimeFormatter |
| `yyyy` vs `uuuu` | Prefer `uuuu` for year |
| Lenient parse accepting `2026-02-31` | `ResolverStyle.STRICT` |
| Format Instant with pattern lacking zone | `withZone` or format ZonedDateTime |

### Related

[instant.md](./instant.md) · [zoneddatetime.md](./zoneddatetime.md) · [interview.md](./interview.md)
