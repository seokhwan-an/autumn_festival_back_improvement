package likelion.festival.like.application;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UuidLikeKeyGenerator implements LikeKeyGenerator {

    @Override
    public String generateLikeKey() {
        return UUID.randomUUID().toString();
    }
}
