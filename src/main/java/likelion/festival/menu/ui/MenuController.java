package likelion.festival.menu.ui;

import likelion.festival.menu.application.MenuService;
import likelion.festival.menu.application.dto.MenuCreateDto;
import likelion.festival.menu.application.dto.MenuResponseDto;
import likelion.festival.menu.application.dto.MenuUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("api/booths/{booth_id}/menus")
@RestController
public class MenuController {

    private final MenuService menuService;

    @GetMapping
    public List<MenuResponseDto> getMenuList(@PathVariable(name = "booth_id") final Long id) {
        return menuService.getAll(id);
    }

    @PostMapping
    public MenuResponseDto createMenu(@PathVariable(name = "booth_id") final Long id, @RequestBody final MenuCreateDto menuCreateDto) {
        return menuService.create(id, menuCreateDto);
    }

    @PutMapping("/{id}")
    public MenuResponseDto updateMenu(@PathVariable final Long id, @RequestBody final MenuUpdateDto menuUpdateDto) {
        return menuService.update(id, menuUpdateDto);
    }

    @DeleteMapping("/{id}")
    public String deleteMenu(@PathVariable final Long id) {
        return menuService.delete(id);
    }
}
