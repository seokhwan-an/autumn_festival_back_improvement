package likelion.festival.global.cookie;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CookieUtil {

    public Map<String, String> changeToMap(Cookie[] cookies) {
        if (cookies == null) {
            return Map.of();
        }
        return Arrays.stream(cookies)
            .collect(Collectors.toMap(Cookie::getName, Cookie::getValue));
    }

    public Cookie generateCookie(String boothId, String cookieKey) {
        Cookie cookie = new Cookie(boothId, cookieKey);
        cookie.setMaxAge(7 * 60 * 60 * 24);
        cookie.setPath("/");
        return cookie;
    }

    public Cookie deleteCookie(String key) {
        Cookie cookie = new Cookie(key, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        return cookie;
    }
}
