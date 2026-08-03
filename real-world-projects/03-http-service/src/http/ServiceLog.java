package http;

import java.time.LocalDateTime;
import java.util.Objects;

public final class ServiceLog {
    public static void info(String msg) {
        System.err.printf("%s INFO  %s%n", LocalDateTime.now(), msg);
    }

    public static void warn(String msg) {
        System.err.printf("%s WARN  %s%n", LocalDateTime.now(), msg);
    }

    public static void error(String msg, Throwable t) {
        System.err.printf("%s ERROR %s: %s%n",
                LocalDateTime.now(), msg, Objects.toString(t.getMessage(), t.toString()));
    }

    private ServiceLog() {}
}
