package likelion.festival.notification.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationTypeTest {

    @DisplayName("공지사항 타입의 이름을 통해 공지사항 타입을 찾아낸다.")
    @ParameterizedTest
    @MethodSource("notificationTypeNameAndType")
    void find_notification_type_by_name(String name, NotificationType type) {
        // given
        // when
        NotificationType result = NotificationType.findByName(name);

        // then
        assertThat(result).isEqualTo(type);
    }

    static Stream<Arguments> notificationTypeNameAndType() {
        return Stream.of(
            Arguments.of("전체", NotificationType.ALL),
            Arguments.of("주요", NotificationType.IMPORTANT),
            Arguments.of("축제", NotificationType.FESTIVAL),
            Arguments.of("이벤트", NotificationType.EVENT),
            Arguments.of("기타", NotificationType.OTHER)
        );
    }

    @DisplayName("잘못된 공지사항 타입 이름으로 공지사항 타입을 조회할 시 예외를 발생한다.")
    @Test
    void find_notification_type_with_wrong_name() {
        // given
        String wrongNotificationTypeName = "없는 공지사항 타입";

        // when
        // then
        Assertions.assertThatThrownBy(() -> NotificationType.findByName(wrongNotificationTypeName))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
