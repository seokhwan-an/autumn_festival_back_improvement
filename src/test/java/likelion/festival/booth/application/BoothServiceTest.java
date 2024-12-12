package likelion.festival.booth.application;

import likelion.festival.booth.application.dto.BoothCreate;
import likelion.festival.booth.application.dto.BoothDayLocationDto;
import likelion.festival.booth.application.dto.BoothFilterDto;
import likelion.festival.booth.application.dto.BoothUpdate;
import likelion.festival.booth.domain.Booth;
import likelion.festival.booth.domain.BoothType;
import likelion.festival.booth.domain.repository.BoothRepository;
import likelion.festival.support.IntegrationTest;
import likelion.festival.support.fixture.BoothFixtureGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class BoothServiceTest extends IntegrationTest {

    @Autowired
    private BoothRepository boothRepository;

    @Autowired
    private BoothService boothService;

    @Autowired
    private BoothFixtureGenerator boothFixtureGenerator;

    @DisplayName("부스 생성")
    @Nested
    class CreateBooth {

        @DisplayName("부스를 생성한다.")
        @Test
        void create_booth() {
            // given
            BoothCreate boothCreate = new BoothCreate("주점", "주점 소개", "주점", "주점 위치", 1, "주점 공지사항", "주점 상세 정보", "2024-11-25", "2024/11/27");

            // when
            Long savedBoothId = boothService.create(boothCreate);

            // then
            Booth result = boothRepository.findById(savedBoothId).get();
            assertAll(
                () -> assertThat(result.getId()).isEqualTo(savedBoothId),
                () -> assertThat(result.getTitle()).isEqualTo(boothCreate.getTitle()),
                () -> assertThat(result.getContent()).isEqualTo(boothCreate.getContent()),
                () -> assertThat(result.getNotice()).isEqualTo(boothCreate.getNotice()),
                () -> assertThat(result.getBoothType()).isEqualTo(BoothType.findByName(boothCreate.getBoothType())),
                () -> assertThat(result.getLocation()).isEqualTo(boothCreate.getLocation()),
                () -> assertThat(result.getBoothNo()).isEqualTo(boothCreate.getBoothNo()),
                () -> assertThat(result.getStartAt()).isEqualTo(boothCreate.getStartAt()),
                () -> assertThat(result.getEndAt()).isEqualTo(boothCreate.getEndAt())
            );
        }
    }

    @DisplayName("부스 조회")
    @Nested
    class ReadBooth {
        @DisplayName("장소와 해당 날짜에 활성화된 부스를 조회한다.")
        @Test
        void read_booth_data() {
            // given
            boothFixtureGenerator.generateDatas();
            String location = "주점 위치0";
            String today = LocalDate.now().toString();

            // when
            List<BoothDayLocationDto> result = boothService.boothDayLocation(today, location);

            // then
            assertAll(
                () -> assertThat(result.get(0).getTitle()).isEqualTo("주점0")
            );
        }


        @DisplayName("title로 부스를 검색한다.")
        @Test
        void booth_search_with_title() {
            // given
            boothFixtureGenerator.generateDatas();
            String title = "주점1";

            // when
            List<BoothFilterDto> result = boothService.boothFilterAndSearch(title);

            // then
            assertAll(
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result.get(0).getTitle()).isEqualTo("주점1")
            );
        }

        @DisplayName("잘못된 title로 부스를 검색한다.")
        @Test
        void booth_search_with_wrong_title() {
            // given
            boothFixtureGenerator.generateDatas();
            String title = "없는 주점";

            // when
            List<BoothFilterDto> result = boothService.boothFilterAndSearch(title);

            // then
            assertThat(result).hasSize(0);

        }

        @DisplayName("location으로 부스를 검색한다.")
        @Test
        void booth_search_with_location() {
            // given
            boothFixtureGenerator.generateDatas();
            String location = "주점 위치1";

            // when
            List<BoothFilterDto> result = boothService.boothFilterAndSearch(location);

            // then
            assertAll(
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result.get(0).getLocation()).isEqualTo("주점 위치1")
            );
        }

        @DisplayName("잘못된 location으로 부스를 검색한다.")
        @Test
        void booth_search_with_wrong_location() {
            // given
            boothFixtureGenerator.generateDatas();
            String location = "없는 주점 위치";

            // when
            List<BoothFilterDto> result = boothService.boothFilterAndSearch(location);

            // then
            assertThat(result).hasSize(0);
        }
    }

    @DisplayName("부스 수정")
    @Nested
    class UpdateBooth {
        @DisplayName("부스 정보를 수정한다.")
        @Test
        void update_booth() {
            // given
            Booth booth = boothFixtureGenerator.generateSingleData();
            BoothUpdate boothUpdate = new BoothUpdate("주점 수정", "주점 소개", "부스", "주점 위치", 1, "주점 공지사항", "주점 상세 정보", "2024-11-25", "2024-11-27");

            // when
            boothService.update(booth.getId(), boothUpdate);

            // then
            Booth result = boothRepository.findById(booth.getId()).get();
            assertAll(
                () -> assertThat(result.getTitle()).isEqualTo(boothUpdate.getTitle()),
                () -> assertThat(result.getContent()).isEqualTo(boothUpdate.getContent()),
                () -> assertThat(result.getNotice()).isEqualTo(boothUpdate.getNotice()),
                () -> assertThat(result.getBoothType()).isEqualTo(BoothType.findByName(boothUpdate.getBoothType())),
                () -> assertThat(result.getLocation()).isEqualTo(boothUpdate.getLocation()),
                () -> assertThat(result.getBoothNo()).isEqualTo(boothUpdate.getBoothNo()),
                () -> assertThat(result.getStartAt()).isEqualTo(boothUpdate.getStartAt()),
                () -> assertThat(result.getEndAt()).isEqualTo(boothUpdate.getEndAt())
            );
        }
    }

    @DisplayName("부스 삭제")
    @Nested
    class DeleteBooth {
        @DisplayName("부스를 삭제한다.")
        @Test
        void delete_booth() {
            // given
            Booth booth = boothFixtureGenerator.generateSingleData();

            // when
            boothService.delete(booth.getId());

            // then
            assertThat(boothRepository.findById(booth.getId())).isEmpty();
        }
    }
}
