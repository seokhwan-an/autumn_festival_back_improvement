package likelion.festival.comment.appliction.dto;

import likelion.festival.comment.domain.Comment;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDto {

    private Long id;

    private String writer;

    private String content;

    private LocalDateTime createdDateTime;

    public static CommentResponseDto of(final Comment comment) {
        return new CommentResponseDto(comment.getId(),
            comment.getWriter(),
            comment.getContent(),
            comment.getCreatedDateTime());
    }
}
