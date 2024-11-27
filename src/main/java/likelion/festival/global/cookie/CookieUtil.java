package likelion.festival.global.cookie;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CookieUtil {

    public Map<String, String> changeToMap(Cookie[] cookies) {
        return Arrays.stream(cookies)
            .collect(Collectors.toMap(Cookie::getName, Cookie::getValue));
    }
}
