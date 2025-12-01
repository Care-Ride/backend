package backend.knowhow.domain.location.repository;

import backend.knowhow.domain.location.domain.Location;
import backend.knowhow.domain.location.dto.LocationMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LocationStore {

    private static final long TTL_MS = 60_000; // 1분 동안 위치 안 오면 제거

    // userId -> Location
    private final Map<Long, Location> store = new ConcurrentHashMap<>();

    public void updateUserLocation(Long userId, LocationMessage locationMessage) {
        store.put(userId, Location.from(locationMessage));
    }

    public Map<Long, Location> getAllLocations() {
        return Map.copyOf(store);
    }

    @Scheduled(fixedRate = TTL_MS) // 1분 주기 정리
    public void cleanupInactiveUsers() {
        long now = System.currentTimeMillis();
        store.entrySet().removeIf(entry ->
                (now - entry.getValue().timestamp()) > TTL_MS
        );
    }
}
