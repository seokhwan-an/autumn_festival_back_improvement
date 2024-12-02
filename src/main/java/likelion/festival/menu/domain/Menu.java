package likelion.festival.menu.domain;

import likelion.festival.booth.domain.Booth;
import lombok.AccessLevel;
import lombok.Builder;
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

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booth_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Booth booth;

    public Menu(final Long id, final String name, final int price, final Booth booth) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.booth = booth;
    }

    public static Menu forSave(final String name, final int price, final Booth booth) {
        return new Menu(null, name, price, booth);
    }

    public void update(final String name, final int price) {
        this.name = name;
        this.price = price;
    }
}
