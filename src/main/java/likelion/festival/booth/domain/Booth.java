package likelion.festival.booth.domain;

import likelion.festival.comment.domain.Comment;
import likelion.festival.like.domain.Likes;
import likelion.festival.menu.domain.Menu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Booth {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    private String notice;

    @Enumerated(EnumType.STRING)
    private BoothType boothType;

    private String introduction;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private Integer boothNo;

    @Column(nullable = false)
    private String startAt;

    @Column(nullable = false)
    private String endAt;

    @OneToMany(mappedBy = "booth", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Menu> menus = new ArrayList<>();

    @OneToMany(mappedBy = "booth", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Likes> likes = new ArrayList<>();

    @OneToMany(mappedBy = "booth", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    // TODO : 이미지는 추후에 처리하기
//    @OneToMany(mappedBy = "booth",cascade = CascadeType.ALL)
//    @JsonIgnore
//    private List<Image> images = new ArrayList<>();

    public Booth(Long id,
                 String title,
                 String content,
                 String notice,
                 BoothType boothType,
                 String introduction,
                 String location,
                 Integer boothNo,
                 String startAt,
                 String endAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.notice = notice;
        this.boothType = boothType;
        this.introduction = introduction;
        this.location = location;
        this.boothNo = boothNo;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public static Booth forSave(String title,
                                String content,
                                String notice,
                                String boothType,
                                String introduction,
                                String location,
                                Integer boothNo,
                                String startAt,
                                String endAt
    ) {
        return new Booth(null,
            title,
            content,
            notice,
            BoothType.valueOf(boothType),
            introduction,
            location,
            boothNo,
            startAt,
            endAt);
    }
}
