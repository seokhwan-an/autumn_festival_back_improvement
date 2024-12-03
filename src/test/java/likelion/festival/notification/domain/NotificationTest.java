package likelion.festival.notification.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationTest {


    @DisplayName("공지사항을 수정한다.")
    @Test
    void update_notification() {
        // given
        Notification notification = new Notification(1L, "공지사항 제목", "작성자", "공지사항 내용", NotificationType.전체);

        // when
        notification.update("공지사항 제목 수정", "작성자", "공지사항 내용 수정", NotificationType.주요);

        // then
        Assertions.assertAll(
            () -> assertThat(notification.getTitle()).isEqualTo("공지사항 제목 수정"),
            () -> assertThat(notification.getWriter()).isEqualTo("작성자"),
            () -> assertThat(notification.getContent()).isEqualTo("공지사항 내용 수정"),
            () -> assertThat(notification.getNotificationType()).isEqualTo(NotificationType.주요)
        );
    }
}
