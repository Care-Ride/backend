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

    public void save(Long memberId, String refreshToken, long days) {
        redisTemplate.opsForValue().set(
                PREFIX + memberId,
                refreshToken,
                Duration.ofDays(days)
        );
    }

    public String find(Long memberId) {
        return redisTemplate.opsForValue().get(PREFIX + memberId);
    }

    public void delete(Long memberId) {
        redisTemplate.delete(PREFIX + memberId);
    }
}
