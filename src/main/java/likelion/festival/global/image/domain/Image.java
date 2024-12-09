package likelion.festival.global.image.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import likelion.festival.booth.domain.Booth;
import likelion.festival.notification.domain.Notification;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Getter
@Entity
@NoArgsConstructor
public class Image {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String originFileName;

    private String serverFileName;

    private String storedFilePath;

    @ManyToOne
    @JsonIgnore
    private Notification notification;

    @ManyToOne
    @JsonIgnore
    private Booth booth;

    @Builder
    public Image(Long id, String originFileName, String serverFileName, String storedFilePath, Notification notification, Booth booth) {
        this.id = id;
        this.originFileName = originFileName;
        this.serverFileName = serverFileName;
        this.storedFilePath = storedFilePath;
        this.notification = notification;
        this.booth = booth;
    }
}
