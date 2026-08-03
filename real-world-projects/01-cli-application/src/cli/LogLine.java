package cli;

import java.time.LocalDateTime;
import java.util.Optional;

public record LogLine(
        Optional<LocalDateTime> timestamp,
        Optional<LogLevel> level,
        String message,
        String raw
) {}
