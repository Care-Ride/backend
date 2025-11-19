package backend.knowhow.domain.member.service;

import backend.knowhow.global.common.exception.BaseException;
import backend.knowhow.global.common.response.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ConnectionCodeService {

    private final StringRedisTemplate redisTemplate;
    private static final int EXPIRE_SECONDS = 300;

    public String generateCode(Long seniorId) {
        String code = String.format("%06d", new Random().nextInt(999999));

        String key = "connection_code:" + code;

        // code -> seniorId 저장
        redisTemplate.opsForValue().set(
                key,
                seniorId.toString(),
                EXPIRE_SECONDS,
                TimeUnit.SECONDS
        );

        return code;
    }

    public Long verifyCode(String code) {
        String key = "connection_code:" + code;
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
