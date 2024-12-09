package likelion.festival.comment.domain.repository;

import likelion.festival.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByBooth_IdAndActiveOrderByCreatedDateTimeDesc(Long id, Boolean active);
}
