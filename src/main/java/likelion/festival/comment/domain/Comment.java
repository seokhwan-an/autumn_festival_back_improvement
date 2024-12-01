package likelion.festival.comment.domain;

import likelion.festival.booth.domain.Booth;
import likelion.festival.global.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String writer;

    private String password;

    private String content;

    private Boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booth_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Booth booth;

    public Comment(final Long id, final String writer, final String password, final String content, final Boolean active, final Booth booth) {
        this.id = id;
        this.writer = writer;
        this.password = password;
        this.content = content;
        this.active = active;
        this.booth = booth;
    }

    public static Comment forSave(final String writer, final String password, final String content, final Booth booth) {
        return new Comment(null, writer, password, content, true, booth);
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
