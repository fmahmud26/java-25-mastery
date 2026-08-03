package events;

import java.time.Instant;
import java.util.Map;

public record Event(
        String id,
        String type,
        Map<String, String> payload,
        Instant createdAt
) {
    /** Sentinel used for graceful consumer shutdown. */
    public static final Event POISON = new Event("__POISON__", "__SHUTDOWN__", Map.of(), Instant.EPOCH);

    public boolean isPoison() {
        return this == POISON || "__POISON__".equals(id);
    }
}
