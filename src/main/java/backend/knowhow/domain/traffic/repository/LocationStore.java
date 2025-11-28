package backend.knowhow.domain.traffic.repository;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LocationStore {

    // userId -> (lat, lon)
    private final Map<Long, double[]> store = new ConcurrentHashMap<>();

    public void updateUserLocation(Long userId, double lat, double lon) {
        store.put(userId, new double[]{lat, lon});
    }

    public Map<Long, double[]> getLocations() {
        return store;
    }
}
