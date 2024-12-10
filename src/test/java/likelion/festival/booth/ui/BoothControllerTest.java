package likelion.festival.booth.ui;

import io.restassured.RestAssured;
import likelion.festival.booth.application.dto.BoothCreate;
import likelion.festival.booth.application.dto.BoothDto;
import likelion.festival.booth.application.dto.BoothUpdate;
import likelion.festival.booth.domain.Booth;
import likelion.festival.booth.domain.BoothType;
import likelion.festival.booth.domain.repository.BoothRepository;
import likelion.festival.like.domain.Likes;
import likelion.festival.support.fixture.BoothFixtureGenerator;
import likelion.festival.support.fixture.LikeFixtureGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BoothControllerTest {

    @Autowired
    private BoothFixtureGenerator boothFixtureGenerator;

    @Autowired
    private LikeFixtureGenerator likeFixtureGenerator;

    @Autowired
    private BoothRepository boothRepository;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @AfterEach
    void cleanUp() {
        boothRepository.deleteAll();
    }

    @DisplayName("부스 생성")
    @Nested
    class CreateBooth {

        @DisplayName("부스 생성 시 201 상태 코드를 반환한다.")
        @Test
        void create_booth() {
            // given
            BoothCreate boothCreate = new BoothCreate("주점", "주점 소개", "주점", "주점 위치", 1, "주점 공지사항", "주점 상세 정보", "2024-11-25", "2024/11/27");

            // when & then
            RestAssured.given()
                .contentType("application/json")
                .body(boothCreate)
                .when()
                .post("/api/booths")
                .then()
                .statusCode(HttpStatus.CREATED.value());
        }
    }

    @DisplayName("부스 조회")
    @Nested
    class ReadBooth {

        @DisplayName("like를 누른 특정 부스를 조회한다.")
        @Test
        void find_liked_booth_by_id() {
            // given
            Booth booth = boothFixtureGenerator.generateSingleData();
            final Likes like = likeFixtureGenerator.generateSingleData(booth);

            // when
            final BoothDto response = RestAssured.given()
                .contentType("application/json")
                .header(HttpHeaders.COOKIE, String.format("%s=%s", booth.getId(), like.getCookieKey()))
                .when()
                .get("/api/booths/" + booth.getId())
                .then()
                .extract()
                .body().as(BoothDto.class);

            // then
            Assertions.assertAll(
                () -> assertThat(response.getId()).isEqualTo(booth.getId()),
                () -> assertThat(response.getIsLike()).isTrue()
            );
        }

        @DisplayName("like를 누르지 않은 특정 부스를 조회한다.")
        @Test
        void find_not_liked_booth_by_id() {
            // given
            Booth booth = boothFixtureGenerator.generateSingleData();
            final Likes like = likeFixtureGenerator.generateSingleData(booth);

            // when
            final BoothDto response = RestAssured.given()
                .contentType("application/json")
                .header(HttpHeaders.COOKIE, String.format("%s=%s", booth.getId() + 1, like.getCookieKey()))
                .when()
                .get("/api/booths/" + booth.getId())
                .then()
                .extract()
                .body().as(BoothDto.class);

            // then
            Assertions.assertAll(
                () -> assertThat(response.getId()).isEqualTo(booth.getId()),
                () -> assertThat(response.getIsLike()).isFalse()
            );
        }

        //TODO: 부스 top5 및 검색 필터 조회 테스트를 추가한다.
    }

    @DisplayName("부스 수정")
    @Nested
    class UpdateBooth {

        @DisplayName("부스 수정 시 상태 코드 200과 변환된 정보를 반환한다.")
        @Test
        void update_booth() {
            // given
            Booth booth = boothFixtureGenerator.generateSingleData();
            BoothUpdate boothUpdate = new BoothUpdate("부스 수정", "부스 소개", "부스", "주점 위치", 1, "주점 공지사항", "주점 상세 정보", "2024-11-25", "2024-11-27");

            // when
            BoothDto response = RestAssured.given()
                .contentType("application/json")
                .body(boothUpdate)
                .when()
                .put("/api/booths/" + booth.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body().as(BoothDto.class);

            // then
            assertAll(
                () -> assertThat(response.getId()).isEqualTo(booth.getId()),
                () -> assertThat(response.getTitle()).isEqualTo(boothUpdate.getTitle()),
                () -> assertThat(response.getContent()).isEqualTo(boothUpdate.getContent()),
                () -> assertThat(response.getNotice()).isEqualTo(boothUpdate.getNotice()),
                () -> assertThat(response.getBoothType()).isEqualTo(BoothType.findByName(boothUpdate.getBoothType())),
                () -> assertThat(response.getLocation()).isEqualTo(boothUpdate.getLocation()),
                () -> assertThat(response.getBoothNo()).isEqualTo(boothUpdate.getBoothNo()),
                () -> assertThat(response.getStartAt()).isEqualTo(boothUpdate.getStartAt()),
                () -> assertThat(response.getEndAt()).isEqualTo(boothUpdate.getEndAt())
            );
        }
    }

    @DisplayName("부스 삭제")
    @Nested
    class DeleteBooth {

        @DisplayName("부스를 삭제한다.")
        @Test
        void delete_booth() {
            // given
            Booth booth = boothFixtureGenerator.generateSingleData();

            // when
            RestAssured.given()
                .contentType("application/json")
                .when()
                .delete("/api/booths/" + booth.getId())
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

            // then
            assertThat(boothRepository.findById(booth.getId())).isEmpty();
        }
    }
}
