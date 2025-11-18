package backend.knowhow.global.config;

import backend.knowhow.domain.member.domain.Role;
import backend.knowhow.global.common.exception.BaseException;
import backend.knowhow.global.common.response.ErrorType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @PostConstruct
    public void init() {
        if (secretKey == null || secretKey.getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalStateException("JWT secret key must be at least 32 bytes");
        }
    }

    private byte[] getSigningKey() {
        return secretKey.getBytes(StandardCharsets.UTF_8);
    }


    public String createAccessToken(Long memberId, Role role) {
        return Jwts.builder()
                .setSubject(String.valueOf(memberId))
                .claim("role", role.name())
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

    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            throw new BaseException(ErrorType.EXPIRED_TOKEN);
        } catch (Exception e) {
            throw new BaseException(ErrorType.INVALID_TOKEN);
        }
    }

    public Long validateAndExtractMemberId(String token) {
        Claims claims = parseClaims(token);
        return Long.valueOf(claims.getSubject());
    }

    public Role extractRole(String token) {
        Claims claims = parseClaims(token);
        String role = claims.get("role", String.class);
        try {
            return Role.valueOf(role);
        } catch (IllegalArgumentException e) {
            throw new BaseException(ErrorType.INVALID_TOKEN);
        }
    }
}
