package likelion.festival.booth.application.dto;

import com.sun.istack.NotNull;
import likelion.festival.booth.domain.Booth;
import likelion.festival.booth.domain.BoothType;
import likelion.festival.global.image.domain.Image;
import lombok.*;

import java.util.List;

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

    public static BoothDayLocationDto of(Booth booth){
        return new BoothDayLocationDto(booth.getId(),
            booth.getBoothType(),
            booth.getTitle(),
            booth.getIntroduction(),
            booth.getLocation(),
            booth.getBoothNo(),
            booth.getLikes().size(),
            false);
    }
}
