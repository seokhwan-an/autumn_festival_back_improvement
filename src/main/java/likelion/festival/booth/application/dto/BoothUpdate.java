package likelion.festival.booth.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BoothUpdate {

    private String title;

    private String introduction;

    private String boothType;

    private String location;

    private Integer boothNo;

    private String notice;

    private String content;

    private String startAt;

    private String endAt;
}
