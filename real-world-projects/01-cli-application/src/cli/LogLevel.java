package cli;

import java.util.Locale;

public enum LogLevel {
    TRACE, DEBUG, INFO, WARN, ERROR, FATAL;

    public static LogLevel parse(String raw) {
        try {
            return LogLevel.valueOf(raw.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Unknown level '" + raw + "'. Expected TRACE|DEBUG|INFO|WARN|ERROR|FATAL");
        }
    }
}
