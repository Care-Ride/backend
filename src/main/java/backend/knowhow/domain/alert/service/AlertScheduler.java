package backend.knowhow.domain.alert.service;

import backend.knowhow.domain.location.domain.Location;
import backend.knowhow.domain.location.repository.LocationStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class AlertScheduler {

    private final LocationStore locationStore;
    private final AlertCollector collector;
    private final AlertFilter filter;
    private final AlertService alertService;

    @Scheduled(fixedRate = 5000) // 5초마다
    public void check() {

        Map<Long, Location> allLocations = locationStore.getAllLocations();
        log.info("🔍 All locations: {}", allLocations);

        for (var entry : allLocations.entrySet()) {

            Long userId = entry.getKey();
            Location location = entry.getValue();

            // 1) 수집 — event/caution
            var alerts = collector.collect(
                    location.lat(), location.lon(),
                    5.0 // 5km 범위
            );

            // 2) 필터
            var filtered = filter.filter(alerts);
            log.info("🔍 All locations: {}", filtered);

            if (!filtered.isEmpty()) {
                alertService.sendAlerts(userId, filtered);
            }
        }
    }
}
