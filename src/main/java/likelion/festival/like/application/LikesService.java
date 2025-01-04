package likelion.festival.like.application;

import likelion.festival.booth.domain.Booth;
import likelion.festival.booth.domain.repository.BoothRepository;
import likelion.festival.booth.exception.BoothErrorCode;
import likelion.festival.booth.exception.BoothException;
import likelion.festival.like.application.dto.LikesResponseDto;
import likelion.festival.like.domain.Likes;
import likelion.festival.like.domain.repository.LikesRepository;
import likelion.festival.like.exception.LikeErrorCode;
import likelion.festival.like.exception.LikeException;
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
            throw new LikeException(LikeErrorCode.ALREADY_LIKED_BOOTH);
        }

        final String newCookieKey = likeKeyGenerator.generateLikeKey();
        final Likes create = Likes.forSave(newCookieKey, booth);
        likesRepository.save(create);

        return LikesResponseDto.of(create);
    }

    public Long delete(final Long boothId, final Map<String, String> likes) {
        final Booth booth = boothRepository.findById(boothId)
            .orElseThrow(() -> new BoothException(BoothErrorCode.NOT_FOUND_BOOTH));

        final Likes like = likesRepository.findByBoothAndCookieKey(booth, likes.get(boothId.toString()))
            .orElseThrow(() -> new LikeException(LikeErrorCode.NOT_LIKED_BOOTH));
        likesRepository.delete(like);
        return booth.getId();
    }
}
