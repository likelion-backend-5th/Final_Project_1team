package mutsa.api.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;
import org.springframework.http.ResponseCookie;
import org.springframework.util.SerializationUtils;

public class CookieUtil {

    public static String REFRESH_TOKEN = "refresh_token";

    public static Optional<Cookie> getCookie(HttpServletRequest request, String key) {
        return Arrays.stream(request.getCookies())
            .filter(cookie -> cookie.getName().equals(key))
            .findAny();
    }

    public static ResponseCookie createCookie(String token) {
        return ResponseCookie.from(JwtTokenProvider.REFRESH_TOKEN, token)
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(Duration.ofDays(30))
            .sameSite("None")
            .build();
    }

    public static void addCookie(HttpServletResponse response, String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(Duration.ofDays(30).toMillisPart());
        response.addCookie(cookie);
    }

    public static void removeCookie(HttpServletRequest request, HttpServletResponse response,
        String name) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                cookie.setValue("");
                cookie.setPath("/");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
    }

    public static String serialize(Object o) {
        return Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(o));
    }

    public static <T> T deserialize(Cookie cookie,
        Class<T> tClass) {
        return tClass.cast(
            SerializationUtils.deserialize(Base64.getUrlDecoder().decode(cookie.getValue())));
    }
}
