package likelion.festival.menu.application;

import likelion.festival.booth.domain.Booth;
import likelion.festival.booth.domain.repository.BoothRepository;
import likelion.festival.global.exception.WrongBoothId;
import likelion.festival.global.exception.WrongMenuId;
import likelion.festival.menu.application.dto.MenuCreateDto;
import likelion.festival.menu.application.dto.MenuResponseDto;
import likelion.festival.menu.domain.Menu;
import likelion.festival.menu.domain.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final BoothRepository boothRepository;

    @Transactional
    public MenuResponseDto create(final Long boothId, final MenuCreateDto menuCreateDto) {
        final Booth booth = boothRepository.findById(boothId)
            .orElseThrow(WrongBoothId::new);
        final Menu newMenu = Menu.forSave(menuCreateDto.getName(), menuCreateDto.getPrice(), booth);
        return MenuResponseDto.of(newMenu);
    }

    public List<MenuResponseDto> getAll(final Long boothId) {
        final Booth booth = boothRepository.findById(boothId)
            .orElseThrow(WrongBoothId::new);

        final List<Menu> menus = menuRepository.findByBooth(booth);
        return menus.stream().map(MenuResponseDto::of)
            .collect(Collectors.toList());
    }

    @Transactional
    public MenuResponseDto update(final Long id, final MenuCreateDto menuCreateDto) {
        final Menu menu = menuRepository.findById(id)
            .orElseThrow(WrongMenuId::new);

        final Menu updateMenu = Menu.forSave(menuCreateDto.getName(), menuCreateDto.getPrice(), menu.getBooth());
        return MenuResponseDto.of(updateMenu);
    }

    @Transactional
    public String delete(final Long id) {
        final Menu menu = menuRepository.findById(id)
            .orElseThrow(WrongMenuId::new);

        menuRepository.delete(menu);
        return "Ok";
    }
}
