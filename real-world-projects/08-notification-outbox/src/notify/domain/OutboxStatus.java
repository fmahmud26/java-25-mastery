package notify.domain;

public enum OutboxStatus {
    NEW,
    IN_FLIGHT,
    SENT,
    DEAD
}
