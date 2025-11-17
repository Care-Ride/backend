package backend.knowhow.global.config;

import backend.knowhow.global.common.exception.BaseException;
import backend.knowhow.global.common.response.ErrorType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    private byte[] getSigningKey() {
        return secretKey.getBytes(StandardCharsets.UTF_8);
    }


    public String createAccessToken(Long memberId) {
        return Jwts.builder()
                .setSubject(String.valueOf(memberId))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1h
                .signWith(Keys.hmacShaKeyFor(getSigningKey()))
                .compact();
    }

    public String createRefreshToken(Long memberId) {
        return Jwts.builder()
                .setSubject(String.valueOf(memberId))
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 14)) // 14일
                .signWith(Keys.hmacShaKeyFor(getSigningKey()))
                .compact();
    }

    public Long validateAndExtractMemberId(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return Long.valueOf(claims.getSubject());

        } catch (io.jsonwebtoken.security.SecurityException |
                 io.jsonwebtoken.MalformedJwtException e) {
            throw new BaseException(ErrorType.INVALID_TOKEN);

        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            throw new BaseException(ErrorType.EXPIRED_TOKEN);

        } catch (Exception e) {
            throw new BaseException(ErrorType.INVALID_TOKEN);
        }
    }
}
