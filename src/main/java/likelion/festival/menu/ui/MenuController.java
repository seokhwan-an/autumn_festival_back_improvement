package likelion.festival.menu.ui;

import likelion.festival.menu.application.MenuService;
import likelion.festival.menu.application.dto.MenuCreateDto;
import likelion.festival.menu.application.dto.MenuResponseDto;
import likelion.festival.menu.application.dto.MenuUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("api/booths/{booth_id}/menus")
@RestController
public class MenuController {

    private final MenuService menuService;

    @PostMapping
    public ResponseEntity<Void> createMenu(@PathVariable(name = "booth_id") final Long id, @RequestBody final MenuCreateDto menuCreateDto) {
        final Long savedMenuId = menuService.create(id, menuCreateDto);
        return ResponseEntity.created(URI.create("/api/booths/" + id + "/menus/" + savedMenuId)).build();
    }

    @GetMapping
    public ResponseEntity<List<MenuResponseDto>> getMenuList(@PathVariable(name = "booth_id") final Long id) {
        final List<MenuResponseDto> response = menuService.getAll(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuResponseDto> updateMenu(@PathVariable final Long id, @RequestBody final MenuUpdateDto menuUpdateDto) {
        final MenuResponseDto response = menuService.update(id, menuUpdateDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenu(@PathVariable final Long id) {
        menuService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
