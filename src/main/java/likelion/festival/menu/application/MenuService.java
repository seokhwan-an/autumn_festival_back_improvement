package likelion.festival.menu.application;

import likelion.festival.booth.domain.Booth;
import likelion.festival.booth.domain.repository.BoothRepository;
import likelion.festival.booth.exception.BoothErrorCode;
import likelion.festival.booth.exception.BoothException;
import likelion.festival.menu.application.dto.MenuCreateDto;
import likelion.festival.menu.application.dto.MenuResponseDto;
import likelion.festival.menu.application.dto.MenuUpdateDto;
import likelion.festival.menu.domain.Menu;
import likelion.festival.menu.domain.repository.MenuRepository;
import likelion.festival.menu.exception.MenuErrorCode;
import likelion.festival.menu.exception.MenuException;
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
    public Long create(final Long boothId, final MenuCreateDto menuCreateDto) {
        final Booth booth = boothRepository.findById(boothId)
            .orElseThrow(() -> new BoothException(BoothErrorCode.NOT_FOUND_BOOTH));
        final Menu newMenu = Menu.forSave(menuCreateDto.getName(), menuCreateDto.getPrice(), booth);
        menuRepository.save(newMenu);
        return newMenu.getId();
    }

    public List<MenuResponseDto> getAll(final Long boothId) {
        final Booth booth = boothRepository.findById(boothId)
            .orElseThrow(() -> new BoothException(BoothErrorCode.NOT_FOUND_BOOTH));

        final List<Menu> menus = menuRepository.findByBooth(booth);
        return menus.stream().map(MenuResponseDto::of)
            .collect(Collectors.toList());
    }

    @Transactional
    public MenuResponseDto update(final Long id, final MenuUpdateDto menuUpdateDto) {
        final Menu menu = menuRepository.findById(id)
            .orElseThrow(() -> new MenuException(MenuErrorCode.NOT_FOUND_MENU));

        menu.update(menuUpdateDto.getName(), menuUpdateDto.getPrice());
        return MenuResponseDto.of(menu);
    }

    @Transactional
    public void delete(final Long id) {
        final Menu menu = menuRepository.findById(id)
            .orElseThrow(() -> new MenuException(MenuErrorCode.NOT_FOUND_MENU));

        menuRepository.delete(menu);
    }
}
