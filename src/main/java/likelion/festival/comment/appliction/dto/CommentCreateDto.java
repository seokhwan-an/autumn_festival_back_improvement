package likelion.festival.comment.appliction.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentCreateDto {

    private String writer;

    private String password;

    private String content;

}
