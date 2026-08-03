package cli;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Minimal structured logger writing to stderr so stdout stays clean for results.
 */
public final class CliLogger {
    public enum Level { DEBUG, INFO, WARN, ERROR }

    private final Level minLevel;

    public CliLogger(Level minLevel) {
        this.minLevel = Objects.requireNonNull(minLevel);
    }

    public static CliLogger info() {
        return new CliLogger(Level.INFO);
    }

    public void debug(String msg) { log(Level.DEBUG, msg); }
    public void info(String msg)  { log(Level.INFO, msg); }
    public void warn(String msg)  { log(Level.WARN, msg); }
    public void error(String msg) { log(Level.ERROR, msg); }

    public void error(String msg, Throwable t) {
        log(Level.ERROR, msg + ": " + t.getMessage());
    }

    private void log(Level level, String msg) {
        if (level.ordinal() < minLevel.ordinal()) {
            return;
        }
        System.err.printf("%s %-5s %s%n", LocalDateTime.now(), level, msg);
    }
}
