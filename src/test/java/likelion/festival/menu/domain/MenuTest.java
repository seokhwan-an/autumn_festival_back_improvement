package likelion.festival.menu.domain;

import likelion.festival.booth.domain.Booth;
import likelion.festival.booth.domain.BoothType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MenuTest {


    @DisplayName("메뉴 정보를 수정한다.")
    @Test
    void update_menu() {
        // give
        Booth booth = new Booth(1L, "주점", "주점 소개", "주점 공지사항", BoothType.부스, "주점 소개", "주점 위치", 1, "2024-11-25", "2024-11-27");
        Menu menu = new Menu(1L, "메뉴 이름", 1000, booth);

        // when
        menu.update("메뉴 이름 변경", 100);

        // then
        Assertions.assertAll(
            () -> assertThat(menu.getName()).isEqualTo("메뉴 이름 변경"),
            () -> assertThat(menu.getPrice()).isEqualTo(100)
        );
    }
}
