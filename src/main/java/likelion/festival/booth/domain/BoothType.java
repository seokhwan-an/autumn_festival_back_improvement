package likelion.festival.booth.domain;

import java.util.Arrays;

public enum BoothType {
    PUB("주점"),
    FOOD_TRUCK("푸드트럭"),
    BOOTH("부스"),
    FLEA_MARKET("플리마켓");

    private final String name;

    BoothType(final String name) {
        this.name = name;
    }

    public static BoothType findByName(final String name) {
        return Arrays.stream(BoothType.values())
            .filter(boothType -> boothType.name.equals(name))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 부스 유형입니다."));
    }
}
