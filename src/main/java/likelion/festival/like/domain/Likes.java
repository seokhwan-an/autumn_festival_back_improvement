package likelion.festival.like.domain;

import likelion.festival.booth.domain.Booth;
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

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cookieKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booth_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Booth booth;

    public Likes(final Long id, final String cookieKey, final Booth booth) {
        this.id = id;
        this.cookieKey = cookieKey;
        this.booth = booth;
    }

    public static Likes forSave(final String cookieKey, final Booth booth) {
        return new Likes(null, cookieKey, booth);
    }
}
