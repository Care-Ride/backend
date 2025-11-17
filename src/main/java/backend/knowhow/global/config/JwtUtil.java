package backend.knowhow.global.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    private final long EXPIRATION = 1000L * 60 * 60 * 24 * 7; // 7일

    @PostConstruct
    private void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }


    public String generateToken(Long userId) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, secretKey)
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
