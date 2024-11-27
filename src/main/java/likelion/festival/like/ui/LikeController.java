package likelion.festival.like.ui;

import likelion.festival.like.application.LikesService;
import likelion.festival.like.application.dto.LikesResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@RequiredArgsConstructor
@RequestMapping("/api/booths/{booth_id}/likes")
@RestController
public class LikeController {

    private final LikesService likesService;

    @PostMapping("/{id}/likes")
    public LikesResponseDto likeCreate(@PathVariable Long id, HttpServletRequest request,
                                       HttpServletResponse response) {
        Optional<Cookie> boothCookie = likesService.findBoothCookie(request, id);
        if (boothCookie.isPresent()) {
            throw new IllegalArgumentException("이미 쿠키 있음");
        }
        LikesResponseDto likes = likesService.create(id);
        Cookie keyCokkie = new Cookie(id.toString(), likes.getCookieKey());
        keyCokkie.setMaxAge(7 * 60 * 60 * 24);
        keyCokkie.setPath("/");
        response.addCookie(keyCokkie);
        return likes;
    }

    @DeleteMapping("/{id}/likes")
    public String likeDelete(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
        Optional<Cookie> boothCookie = likesService.findBoothCookie(request, id);
        if (boothCookie.isPresent()) {
            Cookie userCookie = boothCookie.get();
            String cookieKey = userCookie.getValue();
            likesService.delete(id, cookieKey);

            Cookie keyCookie = new Cookie(id.toString(), null);
            keyCookie.setMaxAge(0);
            keyCookie.setPath("/");
            response.addCookie(keyCookie);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        return "Ok";
    }
}
