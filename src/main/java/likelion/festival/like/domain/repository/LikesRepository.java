package likelion.festival.like.domain.repository;

import likelion.festival.booth.domain.Booth;
import likelion.festival.like.domain.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    Optional<Likes> findByCookieKey(String cookieKey);

    Optional<Likes> findByBoothAndCookieKey(Booth booth, String cookieKey);
}
