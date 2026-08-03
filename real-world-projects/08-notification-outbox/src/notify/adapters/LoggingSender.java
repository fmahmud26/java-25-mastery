package notify.adapters;

import notify.domain.NotificationSender;
import notify.domain.OutboxRecord;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class LoggingSender implements NotificationSender {
    private final Set<String> delivered = ConcurrentHashMap.newKeySet();
    private final boolean failOnce;

    public LoggingSender(boolean failOnce) {
        this.failOnce = failOnce;
    }

    @Override
    public void send(OutboxRecord record) throws Exception {
        if (!delivered.add(record.id())) {
            return; // idempotent effect
        }
        if (failOnce && record.attempts() == 0) {
            delivered.remove(record.id());
            throw new RuntimeException("transient provider failure");
        }
        System.out.println("SENT channel=" + record.channel() + " id=" + record.id() + " payload=" + record.payload());
    }

    public int deliveredCount() {
        return delivered.size();
    }
}
