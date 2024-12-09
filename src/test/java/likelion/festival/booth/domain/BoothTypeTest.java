package likelion.festival.booth.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class BoothTypeTest {

    @DisplayName("부스 종류의 이름을 통해 부스 타입을 찾아낸다.")
    @ParameterizedTest
    @MethodSource("boothNameAndType")
    void find_booth_type_by_name(String name, BoothType type) {
        // given
        // when
        BoothType result = BoothType.findByName(name);

        // then
        assertThat(result).isEqualTo(type);
    }

    static Stream<Arguments> boothNameAndType() {
        return Stream.of(
            Arguments.of("주점", BoothType.PUB),
            Arguments.of("푸드트럭", BoothType.FOOD_TRUCK),
            Arguments.of("부스", BoothType.BOOTH),
            Arguments.of("플리마켓", BoothType.FLEA_MARKET)
        );
    }

    @DisplayName("잘못된 부스 타입 이름으로 부스 타입을 조회할 시 예외를 발생한다.")
    @Test
    void find_booth_type_with_wrong_name() {
        // given
        String wrongBoothTypeName = "없는 부스 타입";

        // when
        // then
        Assertions.assertThatThrownBy(() -> BoothType.findByName(wrongBoothTypeName))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
