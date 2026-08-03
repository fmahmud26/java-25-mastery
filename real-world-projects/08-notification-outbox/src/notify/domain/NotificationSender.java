package notify.domain;

public interface NotificationSender {
    /** Must be idempotent for the same record id. */
    void send(OutboxRecord record) throws Exception;
}
