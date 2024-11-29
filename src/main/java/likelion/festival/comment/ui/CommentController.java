package likelion.festival.comment.ui;

import likelion.festival.comment.appliction.CommentService;
import likelion.festival.comment.appliction.dto.CommentPasswordDto;
import likelion.festival.comment.appliction.dto.CommentRequestDto;
import likelion.festival.comment.appliction.dto.CommentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/booths/{booth_id}/comments")
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> createComment(final @PathVariable(name = "booth_id") Long id,
                                              final @RequestBody CommentRequestDto commentRequestDto,
                                              final HttpServletRequest request
    ) {
        final Long savedCommentId = commentService.create(id, commentRequestDto);
        return ResponseEntity.created(URI.create("/api/booths/" + id + "/comments/" + savedCommentId)).build();
    }

    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> getCommentList(@PathVariable(name = "booth_id") final Long id) {
        final List<CommentResponseDto> response = commentService.getAll(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable(name = "id") Long id,
                                              @RequestBody CommentPasswordDto password) {
        commentService.delete(id, password);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}/force")
    public ResponseEntity<Void> deleteForceComment(@PathVariable(name = "id") Long id) {
        commentService.force_delete(id);
        return ResponseEntity.noContent().build();
    }
}
