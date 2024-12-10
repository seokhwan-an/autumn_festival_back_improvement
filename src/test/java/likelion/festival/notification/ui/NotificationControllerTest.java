package likelion.festival.notification.ui;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import likelion.festival.notification.application.dto.NotificationCreateDto;
import likelion.festival.notification.application.dto.NotificationResponseDto;
import likelion.festival.notification.application.dto.NotificationUpdateDto;
import likelion.festival.notification.domain.Notification;
import likelion.festival.notification.domain.NotificationType;
import likelion.festival.notification.domain.repository.NotificationRepository;
import likelion.festival.support.fixture.NotificationFixtureGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NotificationControllerTest {

    @Autowired
    private NotificationFixtureGenerator notificationFixtureGenerator;

    @Autowired
    private NotificationRepository notificationRepository;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @AfterEach
    void cleanUp() {
        notificationRepository.deleteAll();
    }

    @DisplayName("공지사항 생성")
    @Nested
    class CreateNotification {

        @DisplayName("공지사항 생성 시 201 상태 코드를 반환한다.")
        @Test
        void create_notification() {
            // given
            final NotificationCreateDto request = new NotificationCreateDto("공지사항 제목", "작성자", "공지사항 내용", NotificationType.ALL);

            // when
            final ExtractableResponse<Response> response = RestAssured.given()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/api/notifications")
                .then()
                .extract();

            // then
            assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value())
            );
        }
    }

    @DisplayName("공지사항 조회")
    @Nested
    class ReadNotification {

        @DisplayName("모든 공지사항 조회 시 응답코드 200과 모든 공지사항 정보를 반환한다.")
        @Test
        void read_all_notification() {
            // given
            final List<Notification> notificationsAll = notificationFixtureGenerator.generateDatasWithType(NotificationType.ALL);
            final List<Notification> notificationsMain = notificationFixtureGenerator.generateDatasWithType(NotificationType.IMPORTANT);
            final List<Notification> notificationsFestival = notificationFixtureGenerator.generateDatasWithType(NotificationType.FESTIVAL);
            final List<Notification> notificationsEvent = notificationFixtureGenerator.generateDatasWithType(NotificationType.EVENT);
            final List<Notification> notificationsEct = notificationFixtureGenerator.generateDatasWithType(NotificationType.OTHER);

            // when
            final List<NotificationResponseDto> response = RestAssured.given()
                .contentType("application/json")
                .when()
                .get("/api/notifications")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getList(".", NotificationResponseDto.class);

            final List<Long> result = response.stream()
                .map(NotificationResponseDto::getId)
                .collect(Collectors.toList());

            // then
            assertAll(
                () -> assertThat(result).containsAll(notificationsAll.stream().map(Notification::getId).collect(Collectors.toList())),
                () -> assertThat(result).containsAll(notificationsMain.stream().map(Notification::getId).collect(Collectors.toList())),
                () -> assertThat(result).containsAll(notificationsFestival.stream().map(Notification::getId).collect(Collectors.toList())),
                () -> assertThat(result).containsAll(notificationsEvent.stream().map(Notification::getId).collect(Collectors.toList())),
                () -> assertThat(result).containsAll(notificationsEct.stream().map(Notification::getId).collect(Collectors.toList()))
            );
        }

        @DisplayName("주요 공지사항 조회 시 응답코드 200과 주요 공지사항 정보를 반환한다.")
        @Test
        void read_main_type_notification() {
            // given
            final List<Notification> notificationsAll = notificationFixtureGenerator.generateDatasWithType(NotificationType.ALL);
            final List<Notification> notificationsMain = notificationFixtureGenerator.generateDatasWithType(NotificationType.IMPORTANT);
            final List<Notification> notificationsFestival = notificationFixtureGenerator.generateDatasWithType(NotificationType.FESTIVAL);
            final List<Notification> notificationsEvent = notificationFixtureGenerator.generateDatasWithType(NotificationType.EVENT);
            final List<Notification> notificationsEct = notificationFixtureGenerator.generateDatasWithType(NotificationType.OTHER);

            // when
            final List<NotificationResponseDto> response = RestAssured.given()
                .param("notificationType", "주요")
                .contentType("application/json")
                .when()
                .get("/api/notifications")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getList(".", NotificationResponseDto.class);

            final List<Long> result = response.stream()
                .map(NotificationResponseDto::getId)
                .collect(Collectors.toList());

            // then
            assertAll(
                () -> assertThat(result).doesNotContainSequence(notificationsAll.stream().map(Notification::getId).collect(Collectors.toList())),
                () -> assertThat(result).containsAll(notificationsMain.stream().map(Notification::getId).collect(Collectors.toList())),
                () -> assertThat(result).doesNotContainSequence(notificationsFestival.stream().map(Notification::getId).collect(Collectors.toList())),
                () -> assertThat(result).doesNotContainSequence(notificationsEvent.stream().map(Notification::getId).collect(Collectors.toList())),
                () -> assertThat(result).doesNotContainSequence(notificationsEct.stream().map(Notification::getId).collect(Collectors.toList()))
            );
        }

        @DisplayName("축제 공지사항 조회 시 응답코드 200과 축제 공지사항 정보를 반환한다.")
        @Test
        void read_festival_type_notification() {
            // given
            final List<Notification> notificationsAll = notificationFixtureGenerator.generateDatasWithType(NotificationType.ALL);
            final List<Notification> notificationsMain = notificationFixtureGenerator.generateDatasWithType(NotificationType.IMPORTANT);
            final List<Notification> notificationsFestival = notificationFixtureGenerator.generateDatasWithType(NotificationType.FESTIVAL);
            final List<Notification> notificationsEvent = notificationFixtureGenerator.generateDatasWithType(NotificationType.EVENT);
            final List<Notification> notificationsEct = notificationFixtureGenerator.generateDatasWithType(NotificationType.OTHER);

            // when
            final List<NotificationResponseDto> response = RestAssured.given()
                .param("notificationType", "축제")
                .contentType("application/json")
                .when()
                .get("/api/notifications")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getList(".", NotificationResponseDto.class);

            final List<Long> result = response.stream()
                .map(NotificationResponseDto::getId)
                .collect(Collectors.toList());

            // then
            assertAll(
                () -> assertThat(result).doesNotContainSequence(notificationsAll.stream().map(Notification::getId).collect(Collectors.toList())),
                () -> assertThat(result).doesNotContainSequence(notificationsMain.stream().map(Notification::getId).collect(Collectors.toList())),
                () -> assertThat(result).containsAll(notificationsFestival.stream().map(Notification::getId).collect(Collectors.toList())),
                () -> assertThat(result).doesNotContainSequence(notificationsEvent.stream().map(Notification::getId).collect(Collectors.toList())),
                () -> assertThat(result).doesNotContainSequence(notificationsEct.stream().map(Notification::getId).collect(Collectors.toList()))
            );
        }

        @DisplayName("이벤트 공지사항 조회 시 응답코드 200과 이벤트 공지사항 정보를 반환한다.")
        @Test
        void read_event_type_notification() {
            // given
            final List<Notification> notificationsAll = notificationFixtureGenerator.generateDatasWithType(NotificationType.ALL);
            final List<Notification> notificationsMain = notificationFixtureGenerator.generateDatasWithType(NotificationType.IMPORTANT);
            final List<Notification> notificationsFestival = notificationFixtureGenerator.generateDatasWithType(NotificationType.FESTIVAL);
            final List<Notification> notificationsEvent = notificationFixtureGenerator.generateDatasWithType(NotificationType.EVENT);
            final List<Notification> notificationsEct = notificationFixtureGenerator.generateDatasWithType(NotificationType.OTHER);

            // when
            final List<NotificationResponseDto> response = RestAssured.given()
                .param("notificationType", "이벤트")
                .contentType("application/json")
                .when()
                .get("/api/notifications")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getList(".", NotificationResponseDto.class);

            final List<Long> result = response.stream()
                .map(NotificationResponseDto::getId)
                .collect(Collectors.toList());

            // then
            assertAll(
                () -> assertThat(result).doesNotContainSequence(notificationsAll.stream().map(Notification::getId).collect(Collectors.toList())),
                () -> assertThat(result).doesNotContainSequence(notificationsMain.stream().map(Notification::getId).collect(Collectors.toList())),
                () -> assertThat(result).doesNotContainSequence(notificationsFestival.stream().map(Notification::getId).collect(Collectors.toList())),
                () -> assertThat(result).containsAll(notificationsEvent.stream().map(Notification::getId).collect(Collectors.toList())),
                () -> assertThat(result).doesNotContainSequence(notificationsEct.stream().map(Notification::getId).collect(Collectors.toList()))
            );
        }

        @DisplayName("기타 공지사항 조회 시 응답코드 200과 기타 공지사항 정보를 반환한다.")
        @Test
        void read_etc_type_notification() {
            // given
            final List<Notification> notificationsAll = notificationFixtureGenerator.generateDatasWithType(NotificationType.ALL);
            final List<Notification> notificationsMain = notificationFixtureGenerator.generateDatasWithType(NotificationType.IMPORTANT);
            final List<Notification> notificationsFestival = notificationFixtureGenerator.generateDatasWithType(NotificationType.FESTIVAL);
            final List<Notification> notificationsEvent = notificationFixtureGenerator.generateDatasWithType(NotificationType.EVENT);
            final List<Notification> notificationsEct = notificationFixtureGenerator.generateDatasWithType(NotificationType.OTHER);

            // when
            final List<NotificationResponseDto> response = RestAssured.given()
                .param("notificationType", "기타")
                .contentType("application/json")
                .when()
                .get("/api/notifications")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getList(".", NotificationResponseDto.class);

            final List<Long> result = response.stream()
                .map(NotificationResponseDto::getId)
                .collect(Collectors.toList());

            // then
            assertAll(
                () -> assertThat(result).doesNotContainSequence(notificationsAll.stream().map(Notification::getId).collect(Collectors.toList())),
                () -> assertThat(result).doesNotContainSequence(notificationsMain.stream().map(Notification::getId).collect(Collectors.toList())),
                () -> assertThat(result).doesNotContainSequence(notificationsFestival.stream().map(Notification::getId).collect(Collectors.toList())),
                () -> assertThat(result).doesNotContainSequence(notificationsEvent.stream().map(Notification::getId).collect(Collectors.toList())),
                () -> assertThat(result).containsAll(notificationsEct.stream().map(Notification::getId).collect(Collectors.toList()))
            );
        }

        @DisplayName("id로 공지사항 조회 시 응답코드 200과 단일 공지사항 정보를 반환한다.")
        @Test
        void read_notification_with_id() {
            // given
            final Notification notification = notificationFixtureGenerator.generateSingleDataWithType(NotificationType.ALL);

            // when
            final NotificationResponseDto response = RestAssured.given()
                .contentType("application/json")
                .when()
                .get("/api/notifications/" + notification.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body().as(NotificationResponseDto.class);

            // then
            assertAll(
                () -> assertThat(response.getId()).isEqualTo(notification.getId()),
                () -> assertThat(response.getTitle()).isEqualTo(notification.getTitle()),
                () -> assertThat(response.getWriter()).isEqualTo(notification.getWriter()),
                () -> assertThat(response.getContent()).isEqualTo(notification.getContent()),
                () -> assertThat(response.getNotificationType()).isEqualTo(notification.getNotificationType())
            );
        }
    }

    @DisplayName("공지사항 수정")
    @Nested
    class UpdateNotification {

        @DisplayName("공지사항 수정 시 응답코드 200과 공지사항 정보를 반환한다.")
        @Test
        void update_notification() {
            // given
            final Notification notification = notificationFixtureGenerator.generateSingleDataWithType(NotificationType.ALL);
            final NotificationUpdateDto request = new NotificationUpdateDto("공지사항 수정", notification.getWriter(), "공지사항 내용 수정", notification.getNotificationType());

            // when
            final NotificationResponseDto response = RestAssured.given()
                .contentType("application/json")
                .body(request)
                .when()
                .put("/api/notifications/" + notification.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body().as(NotificationResponseDto.class);

            // then
            assertAll(
                () -> assertThat(response.getId()).isEqualTo(notification.getId()),
                () -> assertThat(response.getTitle()).isEqualTo(request.getTitle()),
                () -> assertThat(response.getWriter()).isEqualTo(request.getWriter()),
                () -> assertThat(response.getContent()).isEqualTo(request.getContent()),
                () -> assertThat(response.getNotificationType()).isEqualTo(request.getNotificationType())
            );
        }
    }

    @DisplayName("공지사항 삭제")
    @Nested
    class DeleteNotification {

        @DisplayName("공지사항 삭제 시 응답코드 204를 반환한다.")
        @Test
        void delete_notification() {
            // given
            final Notification notification = notificationFixtureGenerator.generateSingleDataWithType(NotificationType.ALL);

            // when
            final ExtractableResponse<Response> response = RestAssured.given()
                .contentType("application/json")
                .when()
                .delete("/api/notifications/" + notification.getId())
                .then()
                .extract();

            // then
            assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(notificationRepository.findById(notification.getId())).isEmpty()
            );
        }
    }
}
