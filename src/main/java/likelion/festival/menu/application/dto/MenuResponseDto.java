package likelion.festival.menu.application.dto;

import likelion.festival.menu.domain.Menu;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MenuResponseDto {
    private Long id;

    private String name;

    private int price;

    public static MenuResponseDto of(final Menu menu) {
        return new MenuResponseDto(menu.getId(), menu.getName(), menu.getPrice());
    }
}
