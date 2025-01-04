package likelion.festival.menu.application;

import likelion.festival.booth.domain.Booth;
import likelion.festival.booth.exception.BoothErrorCode;
import likelion.festival.booth.exception.BoothException;
import likelion.festival.menu.application.dto.MenuCreateDto;
import likelion.festival.menu.application.dto.MenuResponseDto;
import likelion.festival.menu.application.dto.MenuUpdateDto;
import likelion.festival.menu.domain.Menu;
import likelion.festival.menu.domain.repository.MenuRepository;
import likelion.festival.menu.exception.MenuErrorCode;
import likelion.festival.menu.exception.MenuException;
import likelion.festival.support.IntegrationTest;
import likelion.festival.support.fixture.BoothFixtureGenerator;
import likelion.festival.support.fixture.MenuFixtureGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class MenuServiceTest extends IntegrationTest {

    @Autowired
    private BoothFixtureGenerator boothFixtureGenerator;

    @Autowired
    private MenuFixtureGenerator menuFixtureGenerator;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuService menuService;

    @DisplayName("메뉴 추가")
    @Nested
    class CreateMenu {

        @DisplayName("메뉴를 추가한다.")
        @Test
        void create_menu() {
            // given
            final Booth booth = boothFixtureGenerator.generateSingleData();
            final MenuCreateDto request = new MenuCreateDto("메뉴 이름", 1000);

            // when
            final Long savedMenuId = menuService.create(booth.getId(), request);

            // then
            final Menu result = menuRepository.findById(savedMenuId).get();
            assertAll(
                () -> assertThat(result.getId()).isEqualTo(savedMenuId),
                () -> assertThat(result.getName()).isEqualTo(request.getName()),
                () -> assertThat(result.getPrice()).isEqualTo(request.getPrice())
            );
        }

        @DisplayName("존재하지 않는 부스에 메뉴를 추가할 수 없다.")
        @Test
        void create_menu_to_not_exist_booth() {
            // given
            final Long wrongBoothId = 9999999L;
            final MenuCreateDto request = new MenuCreateDto("메뉴 이름", 1000);

            // when & then
            assertThatThrownBy(() -> menuService.create(wrongBoothId, request))
                .isInstanceOf(BoothException.class)
                .hasMessage(BoothErrorCode.NOT_FOUND_BOOTH.getMessage());
        }
    }

    @DisplayName("메뉴 조회")
    @Nested
    class ReadMenu {

        @DisplayName("부스에 속한 메뉴를 조회한다.")
        @Test
        void read_menu_with_booth() {
            // given
            final Booth booth = boothFixtureGenerator.generateSingleData();
            final List<Menu> menu = menuFixtureGenerator.generateDatas(booth);

            // when
            final List<MenuResponseDto> response = menuService.getAll(booth.getId());
            final List<Long> result = response.stream()
                .map(MenuResponseDto::getId)
                .collect(Collectors.toList());

            // then
            final List<Long> expect = menu.stream()
                .map(Menu::getId)
                .collect(Collectors.toList());

            assertThat(result).containsAll(expect);
        }

        @DisplayName("존재하지 않는 부스에 속한 메뉴를 조회할 수 없다.")
        @Test
        void read_menu_to_not_exist_booth() {
            // given
            final Long wrongBoothId = 9999999L;

            // when & then
            assertThatThrownBy(() -> menuService.getAll(wrongBoothId))
                .isInstanceOf(BoothException.class)
                .hasMessage(BoothErrorCode.NOT_FOUND_BOOTH.getMessage());
        }
    }

    @DisplayName("메뉴 수정")
    @Nested
    class UpdateMenu {

        @DisplayName("메뉴를 수정한다.")
        @Test
        void update_menu() {
            // given
            final Booth booth = boothFixtureGenerator.generateSingleData();
            final Menu menu = menuFixtureGenerator.generateSingleData(booth);
            final MenuUpdateDto request = new MenuUpdateDto("메뉴 이름 변경", 100);

            // when
            final MenuResponseDto result = menuService.update(menu.getId(), request);

            // then
            final Menu updatedMenu = menuRepository.findById(menu.getId()).get();
            assertAll(
                () -> assertThat(result.getId()).isEqualTo(menu.getId()),
                () -> assertThat(result.getName()).isEqualTo(request.getName()),
                () -> assertThat(result.getPrice()).isEqualTo(request.getPrice()),
                () -> assertThat(updatedMenu.getName()).isEqualTo(request.getName()),
                () -> assertThat(updatedMenu.getPrice()).isEqualTo(request.getPrice())
            );
        }

        @DisplayName("존재하지 않는 메뉴를 수정할 수 없다.")
        @Test
        void update_menu_to_not_exist_menu() {
            // given
            final Long wrongMenuId = 9999999L;
            final MenuUpdateDto request = new MenuUpdateDto("메뉴 이름 변경", 100);

            // when & then
            assertThatThrownBy(() -> menuService.update(wrongMenuId, request))
                .isInstanceOf(MenuException.class)
                .hasMessage(MenuErrorCode.NOT_FOUND_MENU.getMessage());
        }
    }

    @DisplayName("메뉴 삭제")
    @Nested
    class DeleteMenu {

        @DisplayName("메뉴를 삭제한다.")
        @Test
        void delete_menu() {
            // given
            final Booth booth = boothFixtureGenerator.generateSingleData();
            final Menu menu = menuFixtureGenerator.generateSingleData(booth);

            // when
            menuService.delete(menu.getId());

            // then
            assertThat(menuRepository.findById(menu.getId())).isEmpty();
        }

        @DisplayName("없는 메뉴를 삭제할 수 없다.")
        @Test
        void delete_menu_to_not_exist_menu() {
            // given
            final Long wrongMenuId = 9999999L;

            // when & then
            assertThatThrownBy(() -> menuService.delete(wrongMenuId))
                .isInstanceOf(MenuException.class)
                .hasMessage(MenuErrorCode.NOT_FOUND_MENU.getMessage());
        }
    }
}
