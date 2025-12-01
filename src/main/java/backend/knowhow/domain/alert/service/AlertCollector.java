package backend.knowhow.domain.alert.service;

import backend.knowhow.domain.alert.client.CautionSectionApiClient;
import backend.knowhow.domain.alert.client.TrafficEventApiClient;
import backend.knowhow.domain.alert.dto.internal.AlertItem;
import backend.knowhow.domain.alert.mapper.CautionSectionMapper;
import backend.knowhow.domain.alert.mapper.TrafficEventMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AlertCollector {

    private final TrafficEventApiClient trafficEventApiClient;
    private final TrafficEventMapper trafficEventMapper;

    private final CautionSectionApiClient cautionSectionApiClient;
    private final CautionSectionMapper cautionSectionMapper;

    public List<AlertItem> collect(double lat, double lon, double rangeKm) {

        try {
            double d = rangeKm / 111.0;
            double minX = lon - d;
            double maxX = lon + d;
            double minY = lat - d;
            double maxY = lat + d;

            // 1) 돌발상황
            var trafficEvents = trafficEventApiClient.fetchEvents(minX, maxX, minY, maxY);
            var eventAlerts = trafficEventMapper.toAlerts(trafficEvents, lat, lon);

            // 2) 주의운전구간
            var cautionSections = cautionSectionApiClient.fetchCautionSections(minX, maxX, minY, maxY);
            var cautionAlerts = cautionSectionMapper.toAlerts(cautionSections, lat, lon);

            log.info("Collected events={}, cautions={}",
                    eventAlerts.size(), cautionAlerts.size());

            List<AlertItem> combined = new java.util.ArrayList<>(eventAlerts);
            combined.addAll(cautionAlerts);
            return combined;

        } catch (Exception e) {
            log.error("[AlertCollector] Unexpected exception while collecting alerts", e);
            return List.of();
        }
    }
}
