package likelion.festival.like.application;

import likelion.festival.booth.domain.Booth;
import likelion.festival.booth.domain.repository.BoothRepository;
import likelion.festival.booth.exception.BoothErrorCode;
import likelion.festival.booth.exception.BoothException;
import likelion.festival.like.application.dto.LikesResponseDto;
import likelion.festival.like.domain.Likes;
import likelion.festival.like.domain.repository.LikesRepository;
import likelion.festival.support.IntegrationTest;
import likelion.festival.support.fixture.BoothFixtureGenerator;
import likelion.festival.support.fixture.LikeFixtureGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LikesServiceTest extends IntegrationTest {

    @Autowired
    private LikesRepository likesRepository;

    @Autowired
    private BoothRepository boothRepository;

    @Autowired
    private BoothFixtureGenerator boothFixtureGenerator;

    @Autowired
    private LikeFixtureGenerator likeFixtureGenerator;

    private LikesService likesService;

    @BeforeEach
    void setUp() {
        likesService = new LikesService(likesRepository, boothRepository, new staticLikeKeyGenerator());
    }

    @Nested
    @DisplayName("좋아요 추가")
    class CreateLike {

        @DisplayName("booth에 좋아요를 추가한다.")
        @Test
        void create_like_to_booth() {
            // given
            Booth booth = boothFixtureGenerator.generateSingleData();

            // when
            LikesResponseDto result = likesService.create(booth.getId(), Map.of());

            // then
            Assertions.assertAll(
                () -> assertThat(result.getBoothId()).isEqualTo(booth.getId()),
                () -> assertThat(result.getCookieKey()).isEqualTo("boothLikeKey")
            );
        }

        @DisplayName("존재하지 않은 booth에 좋아요를 추가할 수 없다.")
        @Test
        void create_like_to_not_exist_booth() {
            // given
            Long wrongBoothId = 9999999L;

            // when & then
            assertThatThrownBy(() -> likesService.create(wrongBoothId, Map.of()))
                .isInstanceOf(BoothException.class)
                .hasMessage(BoothErrorCode.NOT_FOUND_BOOTH.getMessage());
        }

        @DisplayName("이미 좋아요를 누른 booth에서는 좋아요를 추가할 수 없다.")
        @Test
        void create_like_already_liked_booth() {
            // given
            final Booth booth = boothFixtureGenerator.generateSingleData();

            // when & then
            assertThatThrownBy(() -> likesService.create(booth.getId(), Map.of(booth.getId().toString(), "boothLikeKey2")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 쿠키 있음");
        }
    }

    @Nested
    @DisplayName("좋아요 삭제")
    class DeleteLike {

        @DisplayName("booth에 좋아요를 삭제한다.")
        @Test
        void delete_like_to_booth() {
            // given
            Booth booth = boothFixtureGenerator.generateSingleData();
            Likes like = likeFixtureGenerator.generateSingleData(booth);

            // when
            Long result = likesService.delete(booth.getId(), Map.of(booth.getId().toString(), like.getCookieKey()));

            // then
            Assertions.assertAll(
                () -> assertThat(result).isEqualTo(booth.getId()),
                () -> assertThat(likesRepository.findById(like.getId())).isEmpty()
            );
        }


        @DisplayName("존재하지 않은 booth에 좋아요를 삭제할 수 없다.")
        @Test
        void delete_like_to_not_exist_booth() {
            // given
            Long wrongBoothId = 9999999L;

            // when & then
            assertThatThrownBy(() -> likesService.delete(wrongBoothId, Map.of()))
                .isInstanceOf(BoothException.class)
                .hasMessage(BoothErrorCode.NOT_FOUND_BOOTH.getMessage());
        }

        @DisplayName("부스에 좋아요가 없는 상태에서는 좋아요를 제거할 수 없다.")
        @Test
        void delete_like_not_liked_booth() {
            // given
            final Booth booth = boothFixtureGenerator.generateSingleData();

            // when & then
            assertThatThrownBy(() -> likesService.delete(booth.getId(), Map.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 좋아요 쿠키 없음");
        }
    }

    static class staticLikeKeyGenerator implements LikeKeyGenerator {

        @Override
        public String generateLikeKey() {
            return "boothLikeKey";
        }
    }
}
