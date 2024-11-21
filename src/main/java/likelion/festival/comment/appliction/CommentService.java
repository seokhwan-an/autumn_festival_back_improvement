package likelion.festival.comment.appliction;


import likelion.festival.comment.appliction.dto.CommentPasswordDto;
import likelion.festival.comment.appliction.dto.CommentRequestDto;
import likelion.festival.comment.appliction.dto.CommentResponseDto;
import likelion.festival.booth.domain.Booth;
import likelion.festival.comment.domain.Comment;
import likelion.festival.global.exception.WrongBoothId;
import likelion.festival.global.exception.WrongCommentId;
import likelion.festival.global.exception.WrongPassword;
import likelion.festival.booth.domain.repository.BoothRepository;
import likelion.festival.comment.domain.repository.CommentRepository;
import likelion.festival.global.security.Encrypt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoothRepository boothRepository;
    private final Encrypt encrypt;


    public List<CommentResponseDto> getAll(Long boothId) {
        Optional<Booth> byId = boothRepository.findById(boothId);
        if (byId.isEmpty()) {
            throw new WrongBoothId();
        }
        List<Comment> comments = commentRepository.findByBooth_IdAndActiveOrderByCreatedDateTimeDesc(boothId, Boolean.TRUE);
        return getDtoList(comments);
    }

    @Transactional
    public CommentResponseDto create(Long boothId, CommentRequestDto commentRequestDto, HttpServletRequest request) {
        Optional<Booth> byId = boothRepository.findById(boothId);

        if (byId.isEmpty()) {
            throw new WrongBoothId();
        }
        Booth booth = byId.get();
        commentRequestDto.setBooth(booth);
        commentRequestDto.setIp(getRemoteAddr(request));
        commentRequestDto.setActive(Boolean.TRUE);
        Comment comment = dtoToEntity(commentRequestDto);
        Comment save = commentRepository.save(comment);
        return entityToDto(save);
    }

    @Transactional
    public String delete(Long commentId, CommentPasswordDto password) {
        Optional<Comment> byId = commentRepository.findById(commentId);
        if (byId.isEmpty()) {
            throw new WrongCommentId();
        }
        Comment comment = byId.get();
        if (!comment.getPassword().equals(getEncPwd(password.getPassword()))) {
            throw new WrongPassword();
        }
        comment.setActivte(Boolean.FALSE);
        return "Ok";
    }

    @Transactional
    public String force_delete(Long commentId) {
        Optional<Comment> byId = commentRepository.findById(commentId);
        if (byId.isEmpty()) {
            throw new WrongCommentId();
        }
        Comment comment = byId.get();
        comment.setActivte(Boolean.FALSE);
        return "Ok";
    }


    public Comment dtoToEntity(CommentRequestDto commentRequestDto) {
        String enc_pwd = getEncPwd(commentRequestDto.getPassword());

        return Comment.builder()
                .writer(commentRequestDto.getWriter())
                .password(enc_pwd)
                .content(commentRequestDto.getContent())
                .booth(commentRequestDto.getBooth())
                .ip(commentRequestDto.getIp())
                .active(commentRequestDto.getActive())
                .build();
    }


    private String getEncPwd(String password) {
        return this.encrypt.getEncrypt(password);
    }

    public static String getRemoteAddr(HttpServletRequest request) {
        String ip = null;
        ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-RealIP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("REMOTE_ADDR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }


    public CommentResponseDto entityToDto(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .writer(comment.getWriter())
                .content(comment.getContent())
                .createdDateTime(comment.getCreatedDateTime())
                .build();
    }


    private List<CommentResponseDto> getDtoList(List<Comment> all) {
        return all.stream().map(this::entityToDto)
                .collect(Collectors.toList());
    }

}
