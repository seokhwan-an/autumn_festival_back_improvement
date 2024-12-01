package likelion.festival.support;

import likelion.festival.booth.domain.Booth;
import likelion.festival.comment.domain.Comment;
import likelion.festival.comment.domain.repository.CommentRepository;
import likelion.festival.global.security.Encrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CommentFixtureGenerator {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private Encrypt encrypt;

    public List<Comment> generateDatas(Booth booth) {
        List<Comment> comments = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            boolean active = i % 2 == 0;
            Comment comment = Comment.forSave("작성자" + i, encrypt.getEncrypt("비밀번호" + i), "내용" + i, "ip 주소" + i, active, booth);
            commentRepository.save(comment);
            comments.add(comment);
        }

        return comments;
    }

    public Comment generateSingleData(Booth booth) {
        Comment comment = Comment.forSave("작성자", encrypt.getEncrypt("비밀번호"), "내용", "ip 주소", true, booth);
        return commentRepository.save(comment);
    }
}
