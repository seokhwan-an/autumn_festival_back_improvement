package likelion.festival.like.application.dto;

import likelion.festival.like.domain.Likes;
import lombok.*;

@Getter
@AllArgsConstructor
public class LikesResponseDto {

    private Long boothId;

    private String cookieKey;

    public static LikesResponseDto of(Likes likes) {
        return new LikesResponseDto(likes.getBooth().getId(), likes.getCookieKey());
    }
}
