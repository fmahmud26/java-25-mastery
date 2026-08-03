package cli;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses common log lines:
 *   2026-08-03T10:15:30 INFO  Payment failed for order 42
 *   2026-08-03 10:15:30,123 ERROR Something bad
 */
public final class LogParser {
    private static final DateTimeFormatter ISO_LOCAL =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private static final DateTimeFormatter SPACE_LOCAL =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter SPACE_MILLIS =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,SSS");

    private static final Pattern LINE = Pattern.compile(
            "^(\\d{4}-\\d{2}-\\d{2}[T ]\\d{2}:\\d{2}:\\d{2}(?:,\\d{3})?)\\s+"
                    + "(TRACE|DEBUG|INFO|WARN|ERROR|FATAL)\\s+(.*)$",
            Pattern.CASE_INSENSITIVE);

    private LogParser() {}

    public static LogLine parse(String raw) {
        Matcher m = LINE.matcher(raw.trim());
        if (!m.matches()) {
            return new LogLine(Optional.empty(), Optional.empty(), raw, raw);
        }
        return new LogLine(
                parseTimestamp(m.group(1)),
                Optional.of(LogLevel.parse(m.group(2))),
                m.group(3).trim(),
                raw
        );
    }

    private static Optional<LocalDateTime> parseTimestamp(String ts) {
        try {
            if (ts.contains("T")) {
                return Optional.of(LocalDateTime.parse(ts, ISO_LOCAL));
            }
            if (ts.contains(",")) {
                return Optional.of(LocalDateTime.parse(ts, SPACE_MILLIS));
            }
            return Optional.of(LocalDateTime.parse(ts, SPACE_LOCAL));
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }
}
