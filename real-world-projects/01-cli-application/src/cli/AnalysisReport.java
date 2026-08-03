package cli;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record AnalysisReport(
        long totalLines,
        long matchedLines,
        long unparseable,
        Map<LogLevel, Long> levelCounts,
        List<Map.Entry<String, Long>> topMessages
) {
    public static AnalysisReport of(List<LogLine> lines, Args args) {
        long total = lines.size();
        var filtered = lines.stream().filter(l -> matches(l, args)).toList();
        long unparseable = filtered.stream()
                .filter(l -> l.level().isEmpty())
                .count();

        Map<LogLevel, Long> counts = new EnumMap<>(LogLevel.class);
        for (LogLevel level : LogLevel.values()) {
            counts.put(level, 0L);
        }
        filtered.stream()
                .flatMap(l -> l.level().stream())
                .forEach(level -> counts.merge(level, 1L, Long::sum));

        List<Map.Entry<String, Long>> top = filtered.stream()
                .map(LogLine::message)
                .collect(Collectors.groupingBy(m -> m, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .limit(args.top())
                .toList();

        return new AnalysisReport(total, filtered.size(), unparseable, counts, top);
    }

    private static boolean matches(LogLine line, Args args) {
        if (args.filterLevel().isPresent()) {
            if (line.level().isEmpty() || line.level().get() != args.filterLevel().get()) {
                return false;
            }
        }
        if (args.since().isPresent()) {
            if (line.timestamp().isEmpty() || line.timestamp().get().isBefore(args.since().get())) {
                return false;
            }
        }
        if (args.until().isPresent()) {
            if (line.timestamp().isEmpty() || line.timestamp().get().isAfter(args.until().get())) {
                return false;
            }
        }
        return true;
    }

    public String render() {
        var sb = new StringBuilder();
        sb.append("=== Log Analysis Report ===%n".formatted());
        sb.append("Total lines     : %d%n".formatted(totalLines));
        sb.append("Matched lines   : %d%n".formatted(matchedLines));
        sb.append("Unparseable     : %d%n%n".formatted(unparseable));

        sb.append("Level counts:%n".formatted());
        levelCounts.forEach((level, count) -> {
            if (count > 0) {
                sb.append("  %-5s %d%n".formatted(level, count));
            }
        });

        sb.append("%nTop messages:%n".formatted());
        if (topMessages.isEmpty()) {
            sb.append("  (none)%n".formatted());
        } else {
            int rank = 1;
            for (var e : topMessages) {
                sb.append("  %2d. (%d) %s%n".formatted(rank++, e.getValue(), e.getKey()));
            }
        }
        return sb.toString();
    }

    /** Preserve insertion order for deterministic demo output when needed. */
    public Map<String, Long> topAsMap() {
        Map<String, Long> m = new LinkedHashMap<>();
        for (var e : topMessages) {
            m.put(e.getKey(), e.getValue());
        }
        return m;
    }
}
