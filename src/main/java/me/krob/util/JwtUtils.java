package me.krob.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import me.krob.security.service.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${progress.jwt.secret}")
    private String secret;

    @Value("${progress.jwt.expiration}")
    private int expiration;

    @Value("${progress.jwt.cookie}")
    private String cookie;

    @Value("${progress.jwt.refresh}")
    private String refreshCookie;

    /** Tokens **/

    public String generate(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + expiration))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generate(Authentication authentication) {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();

        return generate(user.getUsername());
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String extract(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validate(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(token);
            return true;
        } catch (Throwable throwable) {
            logger.error("Token error: {}", throwable.getMessage());
        }
        return false;
    }

    /** Cookies **/
    public ResponseCookie generateTokenCookie(UserDetailsImpl userDetails) {
        String token = generate(userDetails.getUsername());
        return generateCookie(cookie, token, "/api");
    }
    public ResponseCookie generateTokenCookie(String username) {
        String token = generate(username);
        return generateCookie(cookie, token, "/api");
    }

    public ResponseCookie generateRefreshCookie(String refreshToken){
        return generateCookie(refreshCookie, refreshToken, "/api/auth/refresh");
    }

    public String getTokenFromCookie(HttpServletRequest request){
        return getCookieByName(request, cookie);
    }

    public String getRefreshFromCookie(HttpServletRequest request){
        return getCookieByName(request, refreshCookie);
    }

    public ResponseCookie getCleanCookie() {
        return ResponseCookie.from(cookie)
                .path("/api")
                .build();
    }

    public ResponseCookie getCleanRefreshCookie() {
        return ResponseCookie.from(cookie)
                .path("/api/auth/refresh")
                .build();
    }

    private ResponseCookie generateCookie(String name, String value, String path){
        return ResponseCookie.from(name, value)
                .path(path)
                .httpOnly(true)
                .build();
    }

    private String getCookieByName(HttpServletRequest request, String name){
        Cookie cookie = WebUtils.getCookie(request, name);
        return cookie != null ? cookie.getValue() : null;
    }
}
