package likelion.festival.notification.application.dto;

import likelion.festival.notification.domain.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class NotificationUpdateDto {

    private String title;

    private String writer;

    private String content;

    private NotificationType notificationType;
}
