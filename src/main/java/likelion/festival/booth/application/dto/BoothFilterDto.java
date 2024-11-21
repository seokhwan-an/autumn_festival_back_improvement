package likelion.festival.booth.application.dto;

import com.sun.istack.NotNull;
import likelion.festival.booth.domain.BoothType;
import likelion.festival.global.image.domain.Image;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class BoothFilterDto {

    private Long id;

    @NotNull
    private BoothType boothType;

    @NotNull
    private String title;

    @NotNull
    private String introduction;

    @NotNull
    private String location;

    private Integer boothNo;

    private Boolean active;

    private Long likeCnt;

    private Boolean isLike;

    private List<Image> images;
}
