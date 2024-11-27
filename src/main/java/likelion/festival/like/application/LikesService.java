package likelion.festival.like.application;

import likelion.festival.booth.domain.Booth;
import likelion.festival.booth.domain.repository.BoothRepository;
import likelion.festival.global.exception.WrongBoothId;
import likelion.festival.global.exception.WrongLikesKey;
import likelion.festival.like.application.dto.LikesResponseDto;
import likelion.festival.like.domain.Likes;
import likelion.festival.like.domain.repository.LikesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LikesService {

    private final LikesRepository likesRepository;
    private final BoothRepository boothRepository;
    private final LikeKeyGenerator likeKeyGenerator;

    public LikesResponseDto create(Long id) {
        Optional<Booth> booth = boothRepository.findById(id);
        if (booth.isEmpty()) {
            throw new WrongBoothId();
        }
        String newCookieKey = likeKeyGenerator.generateLikeKey();
        Likes likes = Likes.forSave(newCookieKey, booth.get());
        likesRepository.save(likes);

        return LikesResponseDto.of(likes);
    }

    public void delete(Long boothId, String cookieKey) {
        Optional<Booth> booth = boothRepository.findById(boothId);
        if (booth.isEmpty()) {
            throw new WrongBoothId();
        }
        Optional<Likes> likes = likesRepository.findByCookieKey(cookieKey);
        if (likes.isEmpty()) {
            throw new WrongLikesKey();
        }
        likesRepository.deleteById(likes.get().getId());
    }

    public Optional<Cookie> findBoothCookie(HttpServletRequest request, Long id) {
        Cookie[] userCookies = request.getCookies();
        if (userCookies == null) {
            return Optional.empty();
        }
        for (Cookie userCookie : userCookies) {
            if (userCookie.getName().equals(id.toString())) {
                return Optional.of(userCookie);
            }
        }
        return Optional.empty();
    }
}
