package likelion.festival.like.ui;

import likelion.festival.global.cookie.CookieUtil;
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
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/api/booths/{booth_id}/likes")
@RestController
public class LikeController {

    private final LikesService likesService;
    private final CookieUtil cookieUtil;

    @PostMapping
    public LikesResponseDto likeCreate(@PathVariable(name = "booth_id") Long id, HttpServletRequest request,
                                       HttpServletResponse response) {
        Map<String, String> likedBooths = cookieUtil.changeToMap(request.getCookies());
        LikesResponseDto responseDto = likesService.create(id, likedBooths);

        Cookie keyCokkie = new Cookie(responseDto.getBoothId().toString(), responseDto.getCookieKey());
        keyCokkie.setMaxAge(7 * 60 * 60 * 24);
        keyCokkie.setPath("/");
        response.addCookie(keyCokkie);
        return responseDto;
    }

    @DeleteMapping
    public String likeDelete(@PathVariable(name = "booth_id") Long id, HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> likedBooths = cookieUtil.changeToMap(request.getCookies());
        Long deletedId = likesService.delete(id, likedBooths);

        Cookie keyCookie = new Cookie(deletedId.toString(), null);
        keyCookie.setMaxAge(0);
        keyCookie.setPath("/");
        response.addCookie(keyCookie);

        return "Ok";
    }
}
