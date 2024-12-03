package likelion.festival.notification.domain.repository;

import likelion.festival.notification.domain.Notification;
import likelion.festival.notification.domain.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAll();

    List<Notification> findByNotificationType(NotificationType notificationType);
}
