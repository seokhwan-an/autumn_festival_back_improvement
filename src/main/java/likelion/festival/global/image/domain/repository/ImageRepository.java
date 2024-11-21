package likelion.festival.global.image.domain.repository;

import likelion.festival.global.image.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
