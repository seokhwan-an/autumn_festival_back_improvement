package likelion.festival.menu.application.dto;

import likelion.festival.menu.domain.Menu;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MenuResponseDto {
    private Long id;

    private String name;

    private Long price;

    public static MenuResponseDto of(final Menu menu) {
        return new MenuResponseDto(menu.getId(), menu.getName(), menu.getPrice());
    }
}
