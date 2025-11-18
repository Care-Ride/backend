package backend.knowhow.domain.auth.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {

    private final StringRedisTemplate redisTemplate;

    private static final String PREFIX = "refresh:";
    private static final long REFRESH_TOKEN_DAYS = 14;

    public void save(Long memberId, String refreshToken) {
        redisTemplate.opsForValue().set(
                PREFIX + memberId,
                refreshToken,
                Duration.ofDays(REFRESH_TOKEN_DAYS)
        );
    }

    public String find(Long memberId) {
        return redisTemplate.opsForValue().get(PREFIX + memberId);
    }

    public void delete(Long memberId) {
        redisTemplate.delete(PREFIX + memberId);
    }
}
