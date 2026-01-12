package backend.knowhow.domain.alert;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AlertHistoryStore {

    // userId -> (message, lastSentTimestamp)
    private final Map<Long, Map<String, Long>> history = new ConcurrentHashMap<>();

    // 동일 메시지 재전송 금지 시간 (ms)
    private static final long COOLDOWN_MS = 60_000; // 1분

    public boolean shouldSend(Long userId, String message) {
        Map<String, Long> userHistory = history.computeIfAbsent(userId,
                k -> new ConcurrentHashMap<>());

        Long lastSent = userHistory.get(message);
        long now = System.currentTimeMillis();

        // 최근에 보낸 적 있음 -> 보내지 않음
        if (lastSent != null && now - lastSent < COOLDOWN_MS) {
            return false;
        }

        // 알림 보낸 시간 갱신
        userHistory.put(message, now);
        return true;
    }

    @Scheduled(fixedRate = COOLDOWN_MS)
    public void cleanupOldHistory() {
        long now = System.currentTimeMillis();

        history.forEach((userId, map) -> {
            map.entrySet().removeIf(e -> now - e.getValue() > COOLDOWN_MS);
        });
    }
}
