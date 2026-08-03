package notify;

import notify.adapters.InMemoryOutboxRepository;
import notify.adapters.LoggingSender;
import notify.application.OutboxPublisher;
import notify.application.ProductNotificationService;

import java.time.Clock;
import java.time.Duration;

public final class NotificationOutboxDemo {
    public static void main(String[] args) {
        var repo = new InMemoryOutboxRepository();
        var sender = new LoggingSender(true); // fail first attempt
        var product = new ProductNotificationService(repo, Clock.systemUTC());
        var publisher = new OutboxPublisher(repo, sender, Clock.systemUTC(), 5, Duration.ofSeconds(30));

        product.notifyUser("sms", "OTP 123456", 100);
        product.notifyUser("email", "Weekly digest", 1);

        System.out.println("poll1 sent=" + publisher.pollOnce(10));
        System.out.println("poll2 sent=" + publisher.pollOnce(10));
        System.out.println("delivered=" + sender.deliveredCount() + " dead=" + publisher.deadCount());
    }
}
