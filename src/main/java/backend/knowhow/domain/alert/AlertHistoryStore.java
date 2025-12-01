package backend.knowhow.domain.alert;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AlertHistoryStore {

    // userId -> (message, lastSentTimestamp)
    private final Map<Long, Map<String, Long>> history = new ConcurrentHashMap<>();

    // 동일 메시지 재전송 금지 시간 (ms)
    private static final long COOLDOWN_MS = 60 * 1000; // 1분

    public boolean shouldSend(Long userId, String message) {

        history.putIfAbsent(userId, new ConcurrentHashMap<>());
        Map<String, Long> userHistory = history.get(userId);

        Long last = userHistory.get(message);

        if (last != null) {
            long elapsed = System.currentTimeMillis() - last;
            if (elapsed < COOLDOWN_MS) {
                return false; // 최근에 보낸 적 있음 -> 보내지 않음
            }
        }

        // 알림 보낸 시간 갱신
        userHistory.put(message, System.currentTimeMillis());
        return true;
    }
}
