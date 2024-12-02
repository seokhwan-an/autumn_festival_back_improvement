package likelion.festival.menu.application;

import likelion.festival.booth.domain.Booth;
import likelion.festival.booth.domain.repository.BoothRepository;
import likelion.festival.global.exception.WrongBoothId;
import likelion.festival.global.exception.WrongMenuId;
import likelion.festival.menu.application.dto.MenuRequestDto;
import likelion.festival.menu.application.dto.MenuResponseDto;
import likelion.festival.menu.domain.Menu;
import likelion.festival.menu.domain.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final BoothRepository boothRepository;

    @Transactional
    public MenuResponseDto create(Long boothId, MenuRequestDto menuRequestDto) {
        Optional<Booth> booth = boothRepository.findById(boothId);
        if (booth.isEmpty()) {
            throw new WrongBoothId();
        }
        Menu newMenu = Menu.forSave(menuRequestDto.getName(), menuRequestDto.getPrice(), booth.get());
        Menu menu = menuRepository.save(newMenu);
        return MenuResponseDto.of(menu);
    }

    public List<MenuResponseDto> getAll(Long boothId) {
        Optional<Booth> booth = boothRepository.findById(boothId);
        if (booth.isEmpty()) {
            throw new WrongBoothId();
        }
        List<Menu> menus = booth.get().getMenus();
        return menus.stream().map(MenuResponseDto::of)
            .collect(Collectors.toList());
    }

    @Transactional
    public MenuResponseDto update(Long id, MenuRequestDto menuRequestDto) {
        Optional<Menu> menu = menuRepository.findById(id);
        if (menu.isEmpty()) {
            throw new WrongMenuId();
        }

        Menu updateMenu = Menu.forSave(menuRequestDto.getName(), menuRequestDto.getPrice(), menu.get().getBooth());
        return MenuResponseDto.of(updateMenu);
    }

    @Transactional
    public String delete(Long id) {
        Optional<Menu> menu = menuRepository.findById(id);
        if (menu.isEmpty()) {
            throw new WrongMenuId();
        }
        menuRepository.delete(menu.get());
        return "Ok";
    }
}
