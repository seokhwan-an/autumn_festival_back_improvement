package likelion.festival.notification.domain;

import java.util.Arrays;

public enum NotificationType {
    ALL("전체"),
    IMPORTANT("주요"),
    FESTIVAL("축제"),
    EVENT("이벤트"),
    OTHER("기타");

    private final String name;

    NotificationType(final String name) {
        this.name = name;
    }

    public static NotificationType findByName(final String name) {
        return Arrays.stream(NotificationType.values())
            .filter(notificationType -> notificationType.name.equals(name))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 알림 유형입니다."));
    }
}
