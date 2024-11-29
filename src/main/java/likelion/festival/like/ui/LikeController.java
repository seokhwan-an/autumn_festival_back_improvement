package likelion.festival.like.ui;

import likelion.festival.global.cookie.CookieUtil;
import likelion.festival.like.application.LikesService;
import likelion.festival.like.application.dto.LikesResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/api/booths/{booth_id}/likes")
@RestController
public class LikeController {

    private final LikesService likesService;
    private final CookieUtil cookieUtil;

    @PostMapping
    public ResponseEntity<LikesResponseDto> likeCreate(@PathVariable(name = "booth_id") final Long id, final HttpServletRequest request,
                                                       HttpServletResponse response) {
        final Map<String, String> likedBooths = cookieUtil.changeToMap(request.getCookies());
        final LikesResponseDto responseDto = likesService.create(id, likedBooths);

        final Cookie likeCookie = cookieUtil.generateCookie(responseDto.getBoothId().toString(), responseDto.getCookieKey());
        response.addCookie(likeCookie);
        return ResponseEntity.created(URI.create("/api/booths/" + id + "/likes/" + responseDto.getId())).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> likeDelete(@PathVariable(name = "booth_id") final Long id, final HttpServletRequest request, final HttpServletResponse response) {
        final Map<String, String> likedBooths = cookieUtil.changeToMap(request.getCookies());
        final Long deletedId = likesService.delete(id, likedBooths);

        final Cookie keyCookie = cookieUtil.deleteCookie(deletedId.toString());
        response.addCookie(keyCookie);

        return ResponseEntity.noContent().build();
    }
}
