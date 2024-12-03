package likelion.festival.notification.application.dto;

import likelion.festival.notification.domain.Notification;
import likelion.festival.notification.domain.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Getter
public class NotificationResponseDto {

    private Long id;

    private String title;

    private String writer;

    private String content;

    private NotificationType notificationType;

    private LocalDateTime createdDateTime;

    private LocalDateTime modifiedDateTime;

    public static NotificationResponseDto of(Notification notificationType) {
        return new NotificationResponseDto(notificationType.getId(),
            notificationType.getTitle(),
            notificationType.getWriter(),
            notificationType.getContent(),
            notificationType.getNotificationType(),
            notificationType.getCreatedDateTime(),
            notificationType.getModifiedDateTime());
    }
}
