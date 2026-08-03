package cli;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Manual argument parser — no external CLI library required.
 */
public final class Args {
    private static final DateTimeFormatter TS =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    private final Path file;
    private final Optional<LogLevel> filterLevel;
    private final int top;
    private final Optional<LocalDateTime> since;
    private final Optional<LocalDateTime> until;
    private final boolean help;

    private Args(Path file,
                 Optional<LogLevel> filterLevel,
                 int top,
                 Optional<LocalDateTime> since,
                 Optional<LocalDateTime> until,
                 boolean help) {
        this.file = file;
        this.filterLevel = filterLevel;
        this.top = top;
        this.since = since;
        this.until = until;
        this.help = help;
    }

    public Path file() { return file; }
    public Optional<LogLevel> filterLevel() { return filterLevel; }
    public int top() { return top; }
    public Optional<LocalDateTime> since() { return since; }
    public Optional<LocalDateTime> until() { return until; }
    public boolean help() { return help; }

    public static Args parse(String[] argv) {
        boolean help = false;
        LogLevel level = null;
        int top = 10;
        LocalDateTime since = null;
        LocalDateTime until = null;
        List<String> positional = new ArrayList<>();

        for (int i = 0; i < argv.length; i++) {
            String a = argv[i];
            switch (a) {
                case "-h", "--help" -> help = true;
                case "--level" -> {
                    requireValue(argv, i, "--level");
                    level = LogLevel.parse(argv[++i]);
                }
                case "--top" -> {
                    requireValue(argv, i, "--top");
                    top = parsePositiveInt(argv[++i], "--top");
                }
                case "--since" -> {
                    requireValue(argv, i, "--since");
                    since = parseTs(argv[++i], "--since");
                }
                case "--until" -> {
                    requireValue(argv, i, "--until");
                    until = parseTs(argv[++i], "--until");
                }
                default -> {
                    if (a.startsWith("-")) {
                        throw new IllegalArgumentException("Unknown option: " + a);
                    }
                    positional.add(a);
                }
            }
        }

        if (help) {
            return new Args(null, Optional.empty(), top, Optional.empty(), Optional.empty(), true);
        }
        if (positional.isEmpty()) {
            throw new IllegalArgumentException("Missing log file path");
        }
        if (positional.size() > 1) {
            throw new IllegalArgumentException("Expected a single log file, got: " + positional);
        }
        if (since != null && until != null && !since.isBefore(until) && !since.equals(until)) {
            throw new IllegalArgumentException("--since must be before or equal to --until");
        }

        return new Args(
                Path.of(positional.getFirst()),
                Optional.ofNullable(level),
                top,
                Optional.ofNullable(since),
                Optional.ofNullable(until),
                false
        );
    }

    public static String helpText() {
        return """
                Log Analyzer — scan application logs for level counts and top messages.

                Usage:
                  LogAnalyzer [options] <logfile>

                Options:
                  --level LEVEL     Filter: TRACE|DEBUG|INFO|WARN|ERROR|FATAL
                  --top N           Show top N messages (default: 10)
                  --since TS        Include lines at/after  yyyy-MM-dd'T'HH:mm:ss
                  --until TS        Include lines at/before yyyy-MM-dd'T'HH:mm:ss
                  -h, --help        Show this help

                Exit codes: 0 ok, 1 usage, 2 I/O, 3 unexpected
                """;
    }

    private static void requireValue(String[] argv, int i, String flag) {
        if (i + 1 >= argv.length) {
            throw new IllegalArgumentException(flag + " requires a value");
        }
    }

    private static int parsePositiveInt(String raw, String flag) {
        try {
            int n = Integer.parseInt(raw);
            if (n <= 0) {
                throw new IllegalArgumentException(flag + " must be positive");
            }
            return n;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(flag + " must be an integer: " + raw);
        }
    }

    private static LocalDateTime parseTs(String raw, String flag) {
        try {
            return LocalDateTime.parse(raw, TS);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    flag + " must be yyyy-MM-dd'T'HH:mm:ss, got: " + raw);
        }
    }
}
