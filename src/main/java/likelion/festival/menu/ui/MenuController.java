package likelion.festival.menu.ui;

import likelion.festival.menu.application.MenuService;
import likelion.festival.menu.application.dto.MenuRequestDto;
import likelion.festival.menu.application.dto.MenuResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("api/booths/{booth_id}/menus")
@RestController
public class MenuController {

    private final MenuService menuService;

    @GetMapping
    public List<MenuResponseDto> getMenuList(@PathVariable(name = "booth_id") Long id) {
        return menuService.getAll(id);
    }

    @PostMapping
    public MenuResponseDto createMenu(@PathVariable(name = "booth_id") Long id, @RequestBody MenuRequestDto menuRequestDto) {
        return menuService.create(id, menuRequestDto);
    }

    @PutMapping("/{id}")
    public MenuResponseDto updateMenu(@PathVariable Long id, @RequestBody MenuRequestDto menuRequestDto){
        return menuService.update(id, menuRequestDto);
    }

    @DeleteMapping("/{id}")
    public String deleteMenu(@PathVariable Long id){
        return menuService.delete(id);
    }
}
