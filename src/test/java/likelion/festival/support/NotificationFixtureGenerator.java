package likelion.festival.support;

import likelion.festival.notification.domain.Notification;
import likelion.festival.notification.domain.NotificationType;
import likelion.festival.notification.domain.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class NotificationFixtureGenerator {

    @Autowired
    private NotificationRepository notificationRepository;

    public List<Notification> generateDatasWithType(final NotificationType notificationType) {
        List<Notification> notifications = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Notification notification = Notification.forSave("공지사항" + i, "작성자" + i, "공지사항 내용" + i, notificationType);
            notificationRepository.save(notification);
            notifications.add(notification);
        }
        return notifications;
    }

    public Notification generateSingleDataWithType(final NotificationType notificationType) {
        Notification notification = Notification.forSave("공지사항", "작성자", "공지사항 내용", notificationType);
        return notificationRepository.save(notification);
    }
}
