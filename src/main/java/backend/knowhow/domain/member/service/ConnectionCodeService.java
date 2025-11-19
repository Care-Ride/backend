package backend.knowhow.domain.member.service;

import backend.knowhow.global.common.exception.BaseException;
import backend.knowhow.global.common.response.ErrorType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ConnectionCodeService {

    private final StringRedisTemplate redisTemplate;
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String PREFIX = "connection_code:";
    @Getter
    @Value("${connection.code.expire-seconds}")
    private int expireSeconds;


    public String generateCode(Long seniorId) {
        String code;
        String key;
        do {
            code = String.format("%06d", RANDOM.nextInt(1_000_000));
            key = PREFIX + code;
        } while (Boolean.TRUE.equals(redisTemplate.hasKey(key)));

        // code -> seniorId 저장
        redisTemplate.opsForValue().set(
                key,
                seniorId.toString(),
                expireSeconds,
                TimeUnit.SECONDS
        );

        return code;
    }

    public Long verifyCode(String code) {
        String key = PREFIX + code;
        String seniorIdStr = redisTemplate.opsForValue().get(key);

        if (seniorIdStr == null) {
            throw new BaseException(ErrorType.INVALID_CONNECTION_CODE);
        }
        return Long.valueOf(seniorIdStr);
    }

    public void deleteCode(String code) {
        String key = "connection_code:" + code;
        redisTemplate.delete(key);
    }

}
