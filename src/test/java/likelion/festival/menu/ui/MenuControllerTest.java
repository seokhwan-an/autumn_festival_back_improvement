package likelion.festival.menu.ui;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import likelion.festival.booth.domain.Booth;
import likelion.festival.menu.application.dto.MenuCreateDto;
import likelion.festival.menu.application.dto.MenuResponseDto;
import likelion.festival.menu.application.dto.MenuUpdateDto;
import likelion.festival.menu.domain.Menu;
import likelion.festival.menu.domain.repository.MenuRepository;
import likelion.festival.support.AcceptanceTest;
import likelion.festival.support.fixture.BoothFixtureGenerator;
import likelion.festival.support.fixture.MenuFixtureGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class MenuControllerTest extends AcceptanceTest {

    @Autowired
    private BoothFixtureGenerator boothFixtureGenerator;

    @Autowired
    private MenuFixtureGenerator menuFixtureGenerator;

    @Autowired
    private MenuRepository menuRepository;

    @DisplayName("부스 생성")
    @Nested
    class CreateMenu {

        @DisplayName("메뉴 생성 시 201 상태 코드를 반환한다.")
        @Test
        void create_menu() {
            // given
            final Booth booth = boothFixtureGenerator.generateSingleData();
            final MenuCreateDto request = new MenuCreateDto("메뉴 이름", 1000);

            // when
            final ExtractableResponse<Response> response = RestAssured.given()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/api/booths/" + booth.getId() + "/menus")
                .then()
                .extract();

            // then
            assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(menuRepository.findByBooth(booth)).isNotEmpty()
            );
        }
    }

    @DisplayName("메뉴 조회")
    @Nested
    class ReadMenu {

        @DisplayName("메뉴를 조회 상태 코드 200을 반환한다.")
        @Test
        void read_menu() {
            // given
            final Booth booth = boothFixtureGenerator.generateSingleData();
            final List<Menu> menus = menuFixtureGenerator.generateDatas(booth);

            // when
            final List<MenuResponseDto> response = RestAssured.given()
                .contentType("application/json")
                .when()
                .get("/api/booths/" + booth.getId() + "/menus")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getList(".", MenuResponseDto.class);

            final List<Long> result = response.stream()
                .map(MenuResponseDto::getId)
                .collect(Collectors.toList());

            // then
            final List<Long> expect = menus.stream()
                .map(Menu::getId)
                .collect(Collectors.toList());

            assertThat(result).containsAll(expect);
        }
    }

    @DisplayName("메뉴 수정")
    @Nested
    class UpdateMenu {

        @DisplayName("메뉴 수정 시 상태 코드 200과 변환된 정보를 반환한다.")
        @Test
        void update_menu() {
            // given
            final Booth booth = boothFixtureGenerator.generateSingleData();
            final Menu menu = menuFixtureGenerator.generateSingleData(booth);
            final MenuUpdateDto request = new MenuUpdateDto("메뉴 이름 변경", 100);

            // when
            final MenuResponseDto response = RestAssured.given()
                .contentType("application/json")
                .body(request)
                .when()
                .put("/api/booths/" + booth.getId() + "/menus/" + menu.getId())
                .then()
                .extract()
                .body().as(MenuResponseDto.class);

            // then
            assertAll(
                () -> assertThat(response.getId()).isEqualTo(menu.getId()),
                () -> assertThat(response.getName()).isEqualTo(request.getName()),
                () -> assertThat(response.getPrice()).isEqualTo(request.getPrice())
            );
        }
    }

    @DisplayName("메뉴 삭제")
    @Nested
    class DeleteMenu {

        @DisplayName("메뉴 삭제 시 상태 코드 204를 반환한다.")
        @Test
        void delete_menu() {
            // given
            final Booth booth = boothFixtureGenerator.generateSingleData();
            final Menu menu = menuFixtureGenerator.generateSingleData(booth);

            // when
            final ExtractableResponse<Response> response = RestAssured.given()
                .contentType("application/json")
                .when()
                .delete("/api/booths/" + booth.getId() + "/menus/" + menu.getId())
                .then()
                .extract();

            // then
            assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(menuRepository.findById(menu.getId())).isEmpty()
            );
        }
    }
}
