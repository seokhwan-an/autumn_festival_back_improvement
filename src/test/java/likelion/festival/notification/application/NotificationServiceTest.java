package likelion.festival.notification.application;

import likelion.festival.global.exception.WrongNotificationId;
import likelion.festival.notification.application.dto.NotificationCreateDto;
import likelion.festival.notification.application.dto.NotificationResponseDto;
import likelion.festival.notification.application.dto.NotificationUpdateDto;
import likelion.festival.notification.domain.Notification;
import likelion.festival.notification.domain.NotificationType;
import likelion.festival.notification.domain.repository.NotificationRepository;
import likelion.festival.support.NotificationFixtureGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class NotificationServiceTest {

    @Autowired
    private NotificationFixtureGenerator notificationFixtureGenerator;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationService notificationService;

    @AfterEach
    void cleanUp() {
        notificationRepository.deleteAll();
    }

    @DisplayName("공지사항 생성")
    @Nested
    class CreateNotification {

        @DisplayName("공지사항을 생성한다.")
        @Test
        void create_notification() {
            // given
            final NotificationCreateDto request = new NotificationCreateDto("공지사항 제목", "작성자", "공지사항 내용", NotificationType.ALL);

            // when
            final Long result = notificationService.createNotification(request);

            // then
            final Notification notification = notificationRepository.findById(result).get();
            assertAll(
                () -> assertThat(notification.getTitle()).isEqualTo(request.getTitle()),
                () -> assertThat(notification.getWriter()).isEqualTo(request.getWriter()),
                () -> assertThat(notification.getContent()).isEqualTo(request.getContent()),
                () -> assertThat(notification.getNotificationType()).isEqualTo(request.getNotificationType())
            );
        }
    }

    @DisplayName("공지사항 조회")
    @Nested
    class ReadNotification {

        @DisplayName("모든 공지사항을 조회한다.")
        @Test
        void read_all_notification() {
            // given
            final List<Notification> notificationsAll = notificationFixtureGenerator.generateDatasWithType(NotificationType.ALL);
            final List<Notification> notificationsMain = notificationFixtureGenerator.generateDatasWithType(NotificationType.IMPORTANT);
            final List<Notification> notificationsFestival = notificationFixtureGenerator.generateDatasWithType(NotificationType.FESTIVAL);
            final List<Notification> notificationsEvent = notificationFixtureGenerator.generateDatasWithType(NotificationType.EVENT);
            final List<Notification> notificationsEct = notificationFixtureGenerator.generateDatasWithType(NotificationType.OTHER);

            // when
            final List<NotificationResponseDto> response = notificationService.readNotificationAll(null);
            final List<Long> result = response.stream()
                .map(NotificationResponseDto::getId)
                .collect(Collectors.toList());
            // then
            Assertions.assertAll(
                () -> assertThat(result).containsAll(notificationsAll.stream().map(Notification::getId).collect(Collectors.toList())),
                () -> assertThat(result).containsAll(notificationsMain.stream().map(Notification::getId).collect(Collectors.toList())),
                () -> assertThat(result).containsAll(notificationsFestival.stream().map(Notification::getId).collect(Collectors.toList())),
                () -> assertThat(result).containsAll(notificationsEvent.stream().map(Notification::getId).collect(Collectors.toList())),
                () -> assertThat(result).containsAll(notificationsEct.stream().map(Notification::getId).collect(Collectors.toList()))
            );
        }

        @DisplayName("주요타입의 공지시항을 조회한다.")
        @Test
        void read_main_type_notification() {
            // given
            final List<Notification> notificationsAll = notificationFixtureGenerator.generateDatasWithType(NotificationType.ALL);
            final List<Notification> notificationsMain = notificationFixtureGenerator.generateDatasWithType(NotificationType.IMPORTANT);
            final List<Notification> notificationsFestival = notificationFixtureGenerator.generateDatasWithType(NotificationType.FESTIVAL);
            final List<Notification> notificationsEvent = notificationFixtureGenerator.generateDatasWithType(NotificationType.EVENT);
            final List<Notification> notificationsEct = notificationFixtureGenerator.generateDatasWithType(NotificationType.OTHER);

            // when
            final List<NotificationResponseDto> response = notificationService.readNotificationAll("주요");
            final List<Long> result = response.stream()
                .map(NotificationResponseDto::getId)
                .collect(Collectors.toList());

            // then
            Assertions.assertAll(
                () -> assertThat(result).doesNotContainSequence(notificationsAll.stream().map(Notification::getId).collect(Collectors.toList())),
                () -> assertThat(result).containsAll(notificationsMain.stream().map(Notification::getId).collect(Collectors.toList())),
                () -> assertThat(result).doesNotContainSequence(notificationsFestival.stream().map(Notification::getId).collect(Collectors.toList())),
                () -> assertThat(result).doesNotContainSequence(notificationsEvent.stream().map(Notification::getId).collect(Collectors.toList())),
                () -> assertThat(result).doesNotContainSequence(notificationsEct.stream().map(Notification::getId).collect(Collectors.toList()))
            );
        }

        @DisplayName("축제 타입의 공지사항을 조회한다.")
        @Test
        void read_festival_type_notification() {
            // given
            final List<Notification> notificationsAll = notificationFixtureGenerator.generateDatasWithType(NotificationType.ALL);
            final List<Notification> notificationsMain = notificationFixtureGenerator.generateDatasWithType(NotificationType.IMPORTANT);
            final List<Notification> notificationsFestival = notificationFixtureGenerator.generateDatasWithType(NotificationType.FESTIVAL);
            final List<Notification> notificationsEvent = notificationFixtureGenerator.generateDatasWithType(NotificationType.EVENT);
            final List<Notification> notificationsEct = notificationFixtureGenerator.generateDatasWithType(NotificationType.OTHER);

            // when
            final List<NotificationResponseDto> response = notificationService.readNotificationAll("축제");
            final List<Long> result = response.stream()
                .map(NotificationResponseDto::getId)
                .collect(Collectors.toList());

            // then
            Assertions.assertAll(
                () -> assertThat(result).doesNotContainSequence(notificationsAll.stream().map(Notification::getId).collect(Collectors.toList())),
                () -> assertThat(result).doesNotContainSequence(notificationsMain.stream().map(Notification::getId).collect(Collectors.toList())),
                () -> assertThat(result).containsAll(notificationsFestival.stream().map(Notification::getId).collect(Collectors.toList())),
                () -> assertThat(result).doesNotContainSequence(notificationsEvent.stream().map(Notification::getId).collect(Collectors.toList())),
                () -> assertThat(result).doesNotContainSequence(notificationsEct.stream().map(Notification::getId).collect(Collectors.toList()))
            );
        }

        @DisplayName("이벤트 타입의 공지사항을 조회한다.")
        @Test
        void read_event_type_notification() {
            // given
            final List<Notification> notificationsAll = notificationFixtureGenerator.generateDatasWithType(NotificationType.ALL);
            final List<Notification> notificationsMain = notificationFixtureGenerator.generateDatasWithType(NotificationType.IMPORTANT);
            final List<Notification> notificationsFestival = notificationFixtureGenerator.generateDatasWithType(NotificationType.FESTIVAL);
            final List<Notification> notificationsEvent = notificationFixtureGenerator.generateDatasWithType(NotificationType.EVENT);
            final List<Notification> notificationsEct = notificationFixtureGenerator.generateDatasWithType(NotificationType.OTHER);

            // when
            final List<NotificationResponseDto> response = notificationService.readNotificationAll("이벤트");
            final List<Long> result = response.stream()
                .map(NotificationResponseDto::getId)
                .collect(Collectors.toList());

            // then
            Assertions.assertAll(
                () -> assertThat(result).doesNotContainSequence(notificationsAll.stream().map(Notification::getId).collect(Collectors.toList())),
                () -> assertThat(result).doesNotContainSequence(notificationsMain.stream().map(Notification::getId).collect(Collectors.toList())),
                () -> assertThat(result).doesNotContainSequence(notificationsFestival.stream().map(Notification::getId).collect(Collectors.toList())),
                () -> assertThat(result).containsAll(notificationsEvent.stream().map(Notification::getId).collect(Collectors.toList())),
                () -> assertThat(result).doesNotContainSequence(notificationsEct.stream().map(Notification::getId).collect(Collectors.toList()))
            );
        }

        @DisplayName("기타 타입의 공지사항을 조회한다.")
        @Test
        void read_etc_type_notification() {
            // given
            final List<Notification> notificationsAll = notificationFixtureGenerator.generateDatasWithType(NotificationType.ALL);
            final List<Notification> notificationsMain = notificationFixtureGenerator.generateDatasWithType(NotificationType.IMPORTANT);
            final List<Notification> notificationsFestival = notificationFixtureGenerator.generateDatasWithType(NotificationType.FESTIVAL);
            final List<Notification> notificationsEvent = notificationFixtureGenerator.generateDatasWithType(NotificationType.EVENT);
            final List<Notification> notificationsEct = notificationFixtureGenerator.generateDatasWithType(NotificationType.OTHER);

            // when
            final List<NotificationResponseDto> response = notificationService.readNotificationAll("기타");
            final List<Long> result = response.stream()
                .map(NotificationResponseDto::getId)
                .collect(Collectors.toList());

            // then
            Assertions.assertAll(
                () -> assertThat(result).doesNotContainSequence(notificationsAll.stream().map(Notification::getId).collect(Collectors.toList())),
                () -> assertThat(result).doesNotContainSequence(notificationsMain.stream().map(Notification::getId).collect(Collectors.toList())),
                () -> assertThat(result).doesNotContainSequence(notificationsFestival.stream().map(Notification::getId).collect(Collectors.toList())),
                () -> assertThat(result).doesNotContainSequence(notificationsEvent.stream().map(Notification::getId).collect(Collectors.toList())),
                () -> assertThat(result).containsAll(notificationsEct.stream().map(Notification::getId).collect(Collectors.toList()))
            );
        }

        @DisplayName("id로 단일 공지사항을 조회한다.")
        @Test
        void read_notification_with_id() {
            // given
            final Notification notification = notificationFixtureGenerator.generateSingleDataWithType(NotificationType.IMPORTANT);

            // when
            final NotificationResponseDto result = notificationService.readNotification(notification.getId());

            // then
            Assertions.assertAll(
                () -> assertThat(result.getId()).isEqualTo(notification.getId()),
                () -> assertThat(result.getTitle()).isEqualTo(notification.getTitle()),
                () -> assertThat(result.getWriter()).isEqualTo(notification.getWriter()),
                () -> assertThat(result.getContent()).isEqualTo(notification.getContent()),
                () -> assertThat(result.getNotificationType()).isEqualTo(notification.getNotificationType())
            );
        }

        @DisplayName("존재하지 않는 id로 공지사항을 조회할 수 없다.")
        @Test
        void read_notification_to_not_exist_id() {
            // given
            final Long wrongId = 9999999L;

            // when & then
            assertThatThrownBy(() -> notificationService.readNotification(wrongId))
                .isInstanceOf(WrongNotificationId.class);
        }
    }

    @DisplayName("공지사항 수정")
    @Nested
    class UpdateNotification {

        @DisplayName("공지사항을 수정한다.")
        @Test
        void update_notification() {
            // given
            final Notification notification = notificationFixtureGenerator.generateSingleDataWithType(NotificationType.IMPORTANT);
            final NotificationUpdateDto request = new NotificationUpdateDto("공지사항 수정", notification.getWriter(), "공지사항 내용 수정", notification.getNotificationType());

            // when
            final NotificationResponseDto result = notificationService.updateNotification(notification.getId(), request);

            // then
            Assertions.assertAll(
                () -> assertThat(result.getId()).isEqualTo(notification.getId()),
                () -> assertThat(result.getTitle()).isEqualTo(request.getTitle()),
                () -> assertThat(result.getWriter()).isEqualTo(request.getWriter()),
                () -> assertThat(result.getContent()).isEqualTo(request.getContent()),
                () -> assertThat(result.getNotificationType()).isEqualTo(request.getNotificationType())
            );
        }

        @DisplayName("존재하지 않는 id로 공지사항을 수정할 수 없다.")
        @Test
        void update_notification_to_not_exist_id() {
            // given
            final Long wrongId = 9999999L;
            final NotificationUpdateDto request = new NotificationUpdateDto("공지사항 수정", "작성자", "공지사항 내용 수정", NotificationType.IMPORTANT);

            // when & then
            assertThatThrownBy(() -> notificationService.updateNotification(wrongId, request))
                .isInstanceOf(WrongNotificationId.class);
        }
    }

    @DisplayName("공지사항 삭제")
    @Nested
    class DeleteNotification {

        @DisplayName("공지사항을 삭제한다.")
        @Test
        void delete_notification() {
            // given
            final Notification notification = notificationFixtureGenerator.generateSingleDataWithType(NotificationType.IMPORTANT);

            // when
            notificationService.deleteNotification(notification.getId());

            // then
            assertThat(notificationRepository.findById(notification.getId())).isEmpty();
        }

        @DisplayName("존재하지 않는 id로 공지사항을 삭제할 수 없다.")
        @Test
        void delete_notification_to_not_exist_id() {
            // given
            final Long wrongId = 9999999L;

            // when & then
            assertThatThrownBy(() -> notificationService.deleteNotification(wrongId))
                .isInstanceOf(WrongNotificationId.class);
        }
    }
}
