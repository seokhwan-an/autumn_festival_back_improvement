package likelion.festival.booth.application.dto;

import likelion.festival.booth.domain.Booth;
import likelion.festival.booth.domain.BoothType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BoothDto {

    private Long id;

    private String title;

    private String introduction;

    private BoothType boothType;

    private String location;

    private Integer boothNo;

    private String notice;

    private String content;

    private String startAt;

    private String endAt;

    private List<Integer> days;

    private long likeCnt;

    private Boolean isLike;

//    private List<Image> images;

    public static BoothDto of(Booth booth, List<Integer> days) {
        return new BoothDto(booth.getId(),
            booth.getTitle(),
            booth.getIntroduction(),
            booth.getBoothType(),
            booth.getLocation(),
            booth.getBoothNo(),
            booth.getNotice(),
            booth.getContent(),
            booth.getStartAt(),
            booth.getEndAt(),
            days,
            booth.getLikes().size(),
            false);
    }

    public void updateIsLike(boolean isLike) {
        this.isLike = isLike;
    }
}
