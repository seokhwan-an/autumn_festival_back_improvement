package likelion.festival.support.fixture;

import likelion.festival.booth.domain.Booth;
import likelion.festival.like.domain.Likes;
import likelion.festival.like.domain.repository.LikesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LikeFixtureGenerator {

    @Autowired
    private LikesRepository likesRepository;

    public Likes generateSingleData(Booth booth) {
        Likes like = Likes.forSave("cookieKey", booth);
        likesRepository.save(like);
        return like;
    }
}
