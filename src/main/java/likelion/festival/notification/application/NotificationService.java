package likelion.festival.notification.application;

import likelion.festival.global.exception.WrongNotificationId;
import likelion.festival.notification.application.dto.NotificationCreateDto;
import likelion.festival.notification.application.dto.NotificationResponseDto;
import likelion.festival.notification.application.dto.NotificationUpdateDto;
import likelion.festival.notification.domain.Notification;
import likelion.festival.notification.domain.NotificationType;
import likelion.festival.notification.domain.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationResponseDto readNotification(final Long id) {
        final Notification notification = notificationRepository.findById(id)
            .orElseThrow(WrongNotificationId::new);
        return NotificationResponseDto.of(notification);
    }

    public List<NotificationResponseDto> readNotificationAll(final String notificationType) {
        if (notificationType == null) {
            final List<Notification> notifications = notificationRepository.findAll();
            return notifications.stream()
                .map(NotificationResponseDto::of)
                .collect(Collectors.toList());
        }

        final List<Notification> notifications = notificationRepository.findByNotificationType(NotificationType.findByName(notificationType));
        return notifications.stream()
            .map(NotificationResponseDto::of)
            .collect(Collectors.toList());
    }

    @Transactional
    public Long createNotification(final NotificationCreateDto request) {
        final Notification notification = Notification.forSave(request.getTitle(),
            request.getWriter(),
            request.getContent(),
            request.getNotificationType());

        notificationRepository.save(notification);
        return notification.getId();
    }

    @Transactional
    public void deleteNotification(final Long id) {
        final Notification notification = notificationRepository.findById(id)
            .orElseThrow(WrongNotificationId::new);

        notificationRepository.delete(notification);
    }

    @Transactional
    public NotificationResponseDto updateNotification(final Long id, final NotificationUpdateDto request) {
        final Notification notification = notificationRepository.findById(id)
            .orElseThrow(WrongNotificationId::new);

        notification.update(request.getTitle(),
            request.getWriter(),
            request.getContent(),
            request.getNotificationType());

        return NotificationResponseDto.of(notification);
    }
}
