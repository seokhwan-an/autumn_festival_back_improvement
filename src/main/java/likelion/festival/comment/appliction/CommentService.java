package likelion.festival.comment.appliction;


import likelion.festival.booth.domain.Booth;
import likelion.festival.booth.domain.repository.BoothRepository;
import likelion.festival.comment.appliction.dto.CommentDeleteDto;
import likelion.festival.comment.appliction.dto.CommentCreateDto;
import likelion.festival.comment.appliction.dto.CommentResponseDto;
import likelion.festival.comment.domain.Comment;
import likelion.festival.comment.domain.repository.CommentRepository;
import likelion.festival.global.exception.WrongBoothId;
import likelion.festival.global.exception.WrongCommentId;
import likelion.festival.global.exception.WrongPassword;
import likelion.festival.global.security.Encrypt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoothRepository boothRepository;
    private final Encrypt encrypt;


    public List<CommentResponseDto> getAll(final Long boothId) {
        final Booth booth = boothRepository.findById(boothId)
            .orElseThrow(WrongBoothId::new);
        final List<Comment> comments = commentRepository.findByBooth_IdAndActiveOrderByCreatedDateTimeDesc(boothId, Boolean.TRUE);
        return comments.stream()
            .map(CommentResponseDto::of)
            .collect(Collectors.toList());
    }

    @Transactional
    public Long create(final Long boothId, final CommentCreateDto commentCreateDto) {
        final Booth booth = boothRepository.findById(boothId)
            .orElseThrow(WrongBoothId::new);

        final Comment comment = Comment.forSave(commentCreateDto.getWriter(),
            commentCreateDto.getPassword(),
            commentCreateDto.getContent(),
            booth);
        commentRepository.save(comment);
        return comment.getId();
    }

    @Transactional
    public void delete(final Long commentId, final CommentDeleteDto password) {
        final Comment comment = commentRepository.findById(commentId)
            .orElseThrow(WrongCommentId::new);
        if (!comment.getPassword().equals(getEncPwd(password.getPassword()))) {
            throw new WrongPassword();
        }
        comment.setActive(Boolean.FALSE);
    }

    @Transactional
    public String force_delete(final Long commentId) {
        final Comment comment = commentRepository.findById(commentId)
            .orElseThrow(WrongCommentId::new);
        commentRepository.delete(comment);
        return "Ok";
    }


    private String getEncPwd(String password) {
        return this.encrypt.getEncrypt(password);
    }
}
