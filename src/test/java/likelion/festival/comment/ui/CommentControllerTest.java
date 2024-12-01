package likelion.festival.comment.ui;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import likelion.festival.booth.domain.Booth;
import likelion.festival.booth.domain.repository.BoothRepository;
import likelion.festival.comment.appliction.dto.CommentPasswordDto;
import likelion.festival.comment.appliction.dto.CommentRequestDto;
import likelion.festival.comment.appliction.dto.CommentResponseDto;
import likelion.festival.comment.domain.Comment;
import likelion.festival.comment.domain.repository.CommentRepository;
import likelion.festival.support.BoothFixtureGenerator;
import likelion.festival.support.CommentFixtureGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CommentControllerTest {

    @Autowired
    private BoothFixtureGenerator boothFixtureGenerator;

    @Autowired
    private CommentFixtureGenerator commentFixtureGenerator;

    @Autowired
    private BoothRepository boothRepository;

    @Autowired
    private CommentRepository commentRepository;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @AfterEach
    void cleanUp() {
        boothRepository.deleteAll();
        commentRepository.deleteAll();
    }

    @DisplayName("댓글 추가")
    @Nested
    class CreateComment {

        @DisplayName("댓글 추가 요청 시 201 상태 코드를 반환한다.")
        @Test
        void create_comment() {
            // given
            final Booth booth = boothFixtureGenerator.generateSingleData();
            CommentRequestDto request = new CommentRequestDto("작성자", "비밀번호", "내용");

            // when
            final ExtractableResponse<Response> response = RestAssured.given()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/api/booths/" + booth.getId() + "/comments")
                .then()
                .extract();

            // then
            Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(commentRepository.findByBooth_IdAndActiveOrderByCreatedDateTimeDesc(booth.getId(), Boolean.TRUE)).isNotEmpty()
            );
        }
    }

    @DisplayName("댓글 조회")
    @Nested
    class GetComment {

        @DisplayName("댓글을 조회 시 활성화된 댓글만 조회하며 응답코드 200을 반환한다.")
        @Test
        void get_comment() {
            // given
            final Booth booth = boothFixtureGenerator.generateSingleData();
            final List<Comment> comments = commentFixtureGenerator.generateDatas(booth);

            // when
            final List<CommentResponseDto> response = RestAssured.given()
                .contentType("application/json")
                .when()
                .get("/api/booths/" + booth.getId() + "/comments")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getList(".", CommentResponseDto.class);

            final List<Long> readResult = response.stream()
                .map(CommentResponseDto::getId)
                .collect(Collectors.toList());

            // then
            final List<Long> activeCommentId = comments.stream()
                .filter(Comment::getActive)
                .map(Comment::getId)
                .collect(Collectors.toList());

            assertThat(readResult).containsAll(activeCommentId);
        }
    }

    @DisplayName("댓글 삭제")
    @Nested
    class UpdateComment {

        @DisplayName("댓글 소프트 삭제 시 상태 코드 204를 반환한다.")
        @Test
        void delete_soft_comment() {
            // given
            final Booth booth = boothFixtureGenerator.generateSingleData();
            final Comment comment = commentFixtureGenerator.generateSingleData(booth);
            CommentPasswordDto request = new CommentPasswordDto("비밀번호");

            // when
            final ExtractableResponse<Response> response = RestAssured.given()
                .contentType("application/json")
                .body(request)
                .when()
                .delete("/api/booths/" + booth.getId() + "/comments/" + comment.getId())
                .then()
                .extract();

            // then
            final Comment deletedComment = commentRepository.findById(comment.getId()).get();
            Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(deletedComment.getActive()).isFalse()
            );
        }

        @DisplayName("댓글 하드 삭제 시 상태 코드 204를 반환한다.")
        @Test
        void delete_force_comment() {
            // given
            final Booth booth = boothFixtureGenerator.generateSingleData();
            final Comment comment = commentFixtureGenerator.generateSingleData(booth);

            // when
            final ExtractableResponse<Response> response = RestAssured.given()
                .contentType("application/json")
                .when()
                .delete("/api/booths/" + booth.getId() + "/comments/" + comment.getId() + "/force")
                .then()
                .extract();

            // then
            ;
            Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(commentRepository.findById(comment.getId())).isEmpty()
            );
        }
    }
}
