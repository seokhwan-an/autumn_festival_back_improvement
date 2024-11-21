package likelion.festival.notification.domain.repository;

import likelion.festival.notification.domain.Notification;
import likelion.festival.notification.domain.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface NotificationRepository extends JpaRepository<Notification,Long> {

    @Override
    ArrayList<Notification> findAll();
    ArrayList<Notification> findByNotificationType(NotificationType notificationType);
}
