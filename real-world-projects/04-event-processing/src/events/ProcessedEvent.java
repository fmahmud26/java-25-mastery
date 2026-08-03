package events;

import java.time.Instant;

public record ProcessedEvent(
        String eventId,
        String type,
        String summary,
        Instant processedAt,
        String worker
) {}
