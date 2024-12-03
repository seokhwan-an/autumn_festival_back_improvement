package likelion.festival.notification.application;

import likelion.festival.global.exception.WrongNotificationId;
import likelion.festival.notification.application.dto.NotificationCreateDto;
import likelion.festival.notification.application.dto.NotificationResponseDto;
import likelion.festival.notification.domain.Notification;
import likelion.festival.notification.domain.NotificationType;
import likelion.festival.notification.domain.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationResponseDto readNotification(Long id) {
        Optional<Notification> notificationOptional = notificationRepository.findById(id);
        if (notificationOptional.isEmpty()) {
            throw new WrongNotificationId();
        }

        Notification notification = notificationOptional.get();
        return NotificationResponseDto.of(notification);
    }

    public List<NotificationResponseDto> readNotificationAll(NotificationType notificationType) {
        List<NotificationResponseDto> notificationResponse = new ArrayList<>();
        if (notificationType == null) {
            List<Notification> notifications = notificationRepository.findAll();
            for (Notification notification : notifications) {
                notificationResponse.add(NotificationResponseDto.of(notification));
            }
            return notificationResponse;
        }
        List<Notification> notifications = notificationRepository.findByNotificationType(notificationType);
        for (Notification notification : notifications) {
            notificationResponse.add(NotificationResponseDto.of(notification));
        }
        return notificationResponse;
    }

    @Transactional
    public Notification createNotification(NotificationCreateDto request) {
        Notification notification = Notification.forSave(request.getTitle(),
            request.getWriter(),
            request.getContent(),
            request.getNotificationType());

        return notificationRepository.save(notification);
    }

    @Transactional
    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }

    @Transactional
    public Notification updateNotification(Long id, NotificationResponseDto notificationResponseDto) {
        Optional<Notification> notification = notificationRepository.findById(id);
        if (notification.isEmpty()) {
            throw new WrongNotificationId();
        }
        notification.get().update(notificationResponseDto.getTitle(),
            notificationResponseDto.getWriter(),
            notificationResponseDto.getContent(),
            notificationResponseDto.getNotificationType());

        return notification.get();
    }
}
