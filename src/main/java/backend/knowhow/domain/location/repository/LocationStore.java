package backend.knowhow.domain.location.repository;

import backend.knowhow.domain.location.domain.Location;
import backend.knowhow.domain.location.dto.LocationMessage;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LocationStore {

    // userId -> Location
    private final Map<Long, Location> store = new ConcurrentHashMap<>();

    public void updateUserLocation(Long userId, LocationMessage locationMessage) {
        store.put(userId, Location.from(locationMessage));
    }

    public Location getLocation(Long userId) {
        return store.get(userId);
    }

    public Map<Long, Location> getAllLocations() {
        return Map.copyOf(store);
    }
}
