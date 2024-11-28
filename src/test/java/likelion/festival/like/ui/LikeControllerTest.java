package likelion.festival.like.ui;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import likelion.festival.booth.domain.Booth;
import likelion.festival.booth.domain.repository.BoothRepository;
import likelion.festival.like.domain.Likes;
import likelion.festival.like.domain.repository.LikesRepository;
import likelion.festival.support.BoothFixtureGenerator;
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


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LikeControllerTest {

    @Autowired
    private BoothFixtureGenerator boothFixtureGenerator;

    @Autowired
    private BoothRepository boothRepository;

    @Autowired
    private LikesRepository likesRepository;

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

    @DisplayName("좋아요 추가")
    @Nested
    class CreateLike {

        @DisplayName("booth에 좋아요를 추가한다.")
        @Test
        void create_like_to_booth() {
            // given
            final Booth booth = boothFixtureGenerator.generateSingleData();

            // when
            final ExtractableResponse<Response> result = RestAssured.given()
                .contentType("application/json")
                .when()
                .post("/api/booths/{booth_id}/likes", booth.getId())
                .then().log().all()
                .extract();

            // then
            Assertions.assertAll(
                () -> assertThat(result.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(result.cookies().containsKey(booth.getId().toString())).isTrue()
            );
        }
    }

    @DisplayName("좋아요 삭제")
    @Nested
    class DeleteLike {

        @DisplayName("booth에 좋아요를 삭제한다.")
        @Test
        void delete_like_to_booth() {
            // given
            final Booth booth = boothFixtureGenerator.generateSingleData();
            final Long boothId = booth.getId();
            Likes like = Likes.forSave("cookieKey", booth);
            likesRepository.save(like);

            // when
            final ExtractableResponse<Response> result = RestAssured.given().log().all()
                .contentType("application/json")
                .header(HttpHeaders.COOKIE, String.format("%s=%s", boothId, "cookieKey"))
                .when()
                .delete("/api/booths/{booth_id}/likes", booth.getId())
                .then().log().all()
                .extract();

            // then
            Assertions.assertAll(
                () -> assertThat(result.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(result.cookies().get("1")).isNullOrEmpty()
            );
        }
    }
}
