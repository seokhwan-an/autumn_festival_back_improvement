package likelion.festival.booth.application.dto;

import likelion.festival.booth.domain.Booth;
import likelion.festival.booth.domain.BoothType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BoothDayLocationDto {

    private Long id;

    private BoothType boothType;

    private String title;

    private String introduction;

    private String location;

    private Integer boothNo;

    private int likeCnt;

    private boolean isLike;

//    private List<Image> images;

    public static BoothDayLocationDto of(final Booth booth) {
        return new BoothDayLocationDto(booth.getId(),
            booth.getBoothType(),
            booth.getTitle(),
            booth.getIntroduction(),
            booth.getLocation(),
            booth.getBoothNo(),
            booth.getLikes().size(),
            false);
    }

    public void updateIsLike(final boolean isLike) {
        this.isLike = isLike;
    }
}
