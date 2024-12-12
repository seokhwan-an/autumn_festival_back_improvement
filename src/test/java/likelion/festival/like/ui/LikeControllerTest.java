package likelion.festival.like.ui;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import likelion.festival.booth.domain.Booth;
import likelion.festival.like.domain.Likes;
import likelion.festival.like.domain.repository.LikesRepository;
import likelion.festival.support.AcceptanceTest;
import likelion.festival.support.fixture.BoothFixtureGenerator;
import likelion.festival.support.fixture.LikeFixtureGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;


class LikeControllerTest extends AcceptanceTest {

    @Autowired
    private BoothFixtureGenerator boothFixtureGenerator;

    @Autowired
    private LikeFixtureGenerator likeFixtureGenerator;

    @Autowired
    private LikesRepository likesRepository;

    @DisplayName("좋아요 추가")
    @Nested
    class CreateLike {

        @DisplayName("booth에 좋아요를 추가한다.")
        @Test
        void create_like_to_booth() {
            // given
            Booth booth = boothFixtureGenerator.generateSingleData();

            // when
            ExtractableResponse<Response> result = RestAssured.given()
                .contentType("application/json")
                .when()
                .post("/api/booths/{booth_id}/likes", booth.getId())
                .then().log().all()
                .extract();

            // then
            String generatedCookieValue = result.cookies().get(booth.getId().toString());
            Assertions.assertAll(
                () -> assertThat(result.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(result.cookies().containsKey(booth.getId().toString())).isTrue(),
                () -> assertThat(likesRepository.findByBoothAndCookieKey(booth, generatedCookieValue)).isNotEmpty()
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
            Booth booth = boothFixtureGenerator.generateSingleData();
            Likes like = likeFixtureGenerator.generateSingleData(booth);

            // when
            final ExtractableResponse<Response> result = RestAssured.given().log().all()
                .contentType("application/json")
                .header(HttpHeaders.COOKIE, String.format("%s=%s", booth.getId(), like.getCookieKey()))
                .when()
                .delete("/api/booths/{booth_id}/likes", booth.getId())
                .then().log().all()
                .extract();

            // then
            Assertions.assertAll(
                () -> assertThat(result.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(result.cookies().get(booth.getId().toString())).isNullOrEmpty(),
                () -> assertThat(likesRepository.findByBoothAndCookieKey(booth, like.getCookieKey())).isEmpty()
            );
        }
    }
}
