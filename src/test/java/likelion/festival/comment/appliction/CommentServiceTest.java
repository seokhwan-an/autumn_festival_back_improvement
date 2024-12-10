package likelion.festival.comment.appliction;

import likelion.festival.booth.domain.Booth;
import likelion.festival.booth.domain.repository.BoothRepository;
import likelion.festival.comment.appliction.dto.CommentCreateDto;
import likelion.festival.comment.appliction.dto.CommentDeleteDto;
import likelion.festival.comment.appliction.dto.CommentResponseDto;
import likelion.festival.comment.domain.Comment;
import likelion.festival.comment.domain.repository.CommentRepository;
import likelion.festival.global.exception.WrongBoothId;
import likelion.festival.global.exception.WrongCommentId;
import likelion.festival.global.exception.WrongPassword;
import likelion.festival.support.fixture.BoothFixtureGenerator;
import likelion.festival.support.fixture.CommentFixtureGenerator;
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

@SpringBootTest
class CommentServiceTest {

    @Autowired
    private BoothFixtureGenerator boothFixtureGenerator;

    @Autowired
    private CommentFixtureGenerator commentFixtureGenerator;

    @Autowired
    private BoothRepository boothRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentService commentService;

    @AfterEach
    void cleanUp() {
        boothRepository.deleteAll();
        commentRepository.deleteAll();
    }


    @DisplayName("댓글 추가")
    @Nested
    class CreateComment {

        @DisplayName("댓글을 추가한다.")
        @Test
        void create_comment() {
            // given
            final Booth booth = boothFixtureGenerator.generateSingleData();
            final CommentCreateDto request = new CommentCreateDto("작성자", "비밀번호", "내용");

            // when
            final Long result = commentService.create(booth.getId(), request);

            // then
            final Comment comment = commentRepository.findById(result).get();
            Assertions.assertAll(
                () -> assertThat(comment.getWriter()).isEqualTo(request.getWriter()),
                () -> assertThat(comment.getPassword()).isEqualTo(request.getPassword()),
                () -> assertThat(comment.getContent()).isEqualTo(request.getContent()),
                () -> assertThat(comment.getActive()).isTrue()
            );
        }

        @DisplayName("존재하지 않은 부스에 댓글을 추가할 수 없다.")
        @Test
        void create_comment_to_not_exist_booth() {
            // given
            final Long wrongBoothId = 9999999L;
            final CommentCreateDto request = new CommentCreateDto("작성자", "비밀번호", "내용");

            // when & then
            assertThatThrownBy(() -> commentService.create(wrongBoothId, request))
                .isInstanceOf(WrongBoothId.class);

        }
    }

    @DisplayName("댓글 조회")
    @Nested
    class UpdateComment {

        @DisplayName("활성화 된 댓글들만 조회한다.")
        @Test
        void update_comment() {
            // given
            final Booth booth = boothFixtureGenerator.generateSingleData();
            final List<Comment> comment = commentFixtureGenerator.generateDatas(booth);

            // when
            final List<CommentResponseDto> readCommentWithBoothId = commentService.getAll(booth.getId());
            final List<Long> result = readCommentWithBoothId.stream()
                .map(CommentResponseDto::getId)
                .collect(Collectors.toList());

            // then
            final List<Long> savedCommentIds = comment.stream()
                .filter(Comment::getActive)
                .map(Comment::getId)
                .collect(Collectors.toList());

            assertThat(result).containsAll(savedCommentIds);
        }
    }

    @DisplayName("댓글 삭제")
    @Nested
    class DeleteComment {

        @DisplayName("댓글의 소프트 delete를 진행한다.")
        @Test
        void delete_comment() {
            // given
            final Booth booth = boothFixtureGenerator.generateSingleData();
            final Comment comment = commentFixtureGenerator.generateSingleData(booth);
            CommentDeleteDto deleteRequest = new CommentDeleteDto("비밀번호");

            // when
            commentService.delete(comment.getId(), deleteRequest);


            // then
            final Comment deletedComment = commentRepository.findById(comment.getId()).get();
            assertThat(deletedComment.getActive()).isFalse();
        }

        @DisplayName("잘못된 비밀번호로는 댓글을 삭제할 수 없다.")
        @Test
        void delete_comment_with_wrong_password() {
            // given
            final Booth booth = boothFixtureGenerator.generateSingleData();
            final Comment comment = commentFixtureGenerator.generateSingleData(booth);
            CommentDeleteDto deleteRequest = new CommentDeleteDto("없는 비밀번호");

            // when & then
            assertThatThrownBy(() -> commentService.delete(comment.getId(), deleteRequest))
                .isInstanceOf(WrongPassword.class);

        }

        @DisplayName("댓글을 db에서 삭제한다.")
        @Test
        void delete_force_comment() {
            // given
            final Booth booth = boothFixtureGenerator.generateSingleData();
            final Comment comment = commentFixtureGenerator.generateSingleData(booth);

            // when
            commentService.force_delete(comment.getId());

            // then
            assertThat(commentRepository.findById(comment.getId())).isEmpty();
        }

        @DisplayName("존재하지 않는 댓글을 삭제할 수 없다.")
        @Test
        void delete_force_comment_to_not_exist_comment() {
            // given
            final Long wrongCommentId = 9999999L;

            // when & then
            assertThatThrownBy(() -> commentService.force_delete(wrongCommentId))
                .isInstanceOf(WrongCommentId.class);
        }
    }
}
