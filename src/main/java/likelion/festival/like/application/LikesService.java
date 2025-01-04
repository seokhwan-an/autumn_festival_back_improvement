package likelion.festival.like.application;

import likelion.festival.booth.domain.Booth;
import likelion.festival.booth.domain.repository.BoothRepository;
import likelion.festival.booth.exception.BoothErrorCode;
import likelion.festival.booth.exception.BoothException;
import likelion.festival.global.exception.WrongLikesKey;
import likelion.festival.like.application.dto.LikesResponseDto;
import likelion.festival.like.domain.Likes;
import likelion.festival.like.domain.repository.LikesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class LikesService {

    private final LikesRepository likesRepository;
    private final BoothRepository boothRepository;
    private final LikeKeyGenerator likeKeyGenerator;

    public LikesResponseDto create(final Long id, final Map<String, String> likes) {
        final Booth booth = boothRepository.findById(id)
            .orElseThrow(() -> new BoothException(BoothErrorCode.NOT_FOUND_BOOTH));

        if (likes.containsKey(id.toString())) {
            throw new IllegalArgumentException("이미 쿠키 있음");
        }

        final String newCookieKey = likeKeyGenerator.generateLikeKey();
        final Likes create = Likes.forSave(newCookieKey, booth);
        likesRepository.save(create);

        return LikesResponseDto.of(create);
    }

    public Long delete(final Long boothId, final Map<String, String> likes) {
        final Booth booth = boothRepository.findById(boothId)
            .orElseThrow(() -> new BoothException(BoothErrorCode.NOT_FOUND_BOOTH));

        if (!likes.containsKey(boothId.toString())) {
            throw new IllegalArgumentException("해당 좋아요 쿠키 없음");
        }

        final Likes like = likesRepository.findByBoothAndCookieKey(booth, likes.get(boothId.toString()))
            .orElseThrow(WrongLikesKey::new);
        likesRepository.delete(like);
        return booth.getId();
    }
}
