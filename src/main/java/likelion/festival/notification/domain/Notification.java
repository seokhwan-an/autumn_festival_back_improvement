package likelion.festival.notification.domain;

import likelion.festival.global.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String writer;

    private String content;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    // TODO : 이미지는 추후에 처리하기
//    @OneToMany(mappedBy = "notification",cascade = CascadeType.ALL)
//    @JsonIgnore
//    private List<Image> images = new ArrayList<>();

    @Builder
    public Notification(final Long id, final String title, final String writer, final String content, final NotificationType notificationType) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.content = content;
        this.notificationType = notificationType;
    }

    public static Notification forSave(final String title, final String writer, final String content, final NotificationType notificationType) {
        return new Notification(null, title, writer, content, notificationType);
    }

    public void update(final String title, final String writer, final String content, final NotificationType notificationType) {
        this.title = title;
        this.writer = writer;
        this.content = content;
        this.notificationType = notificationType;
    }
}
