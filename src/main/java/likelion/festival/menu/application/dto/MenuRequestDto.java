package likelion.festival.menu.application.dto;

import likelion.festival.booth.domain.Booth;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuRequestDto {
    private Long id;

    private String name;

    private Long price;

    private Booth booth;
}
