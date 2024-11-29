package likelion.festival.like.application.dto;

import likelion.festival.like.domain.Likes;
import lombok.*;

@Getter
@AllArgsConstructor
public class LikesResponseDto {

    private Long id;

    private Long boothId;

    private String cookieKey;

    public static LikesResponseDto of(final Likes likes) {
        return new LikesResponseDto(likes.getId(), likes.getBooth().getId(), likes.getCookieKey());
    }
}
