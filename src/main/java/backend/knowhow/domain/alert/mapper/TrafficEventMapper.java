package backend.knowhow.domain.alert.mapper;

import backend.knowhow.domain.alert.dto.external.TrafficEventItem;
import backend.knowhow.domain.alert.dto.internal.AlertItem;
import backend.knowhow.domain.alert.util.DistanceCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TrafficEventMapper {

    private final DistanceCalculator distanceCalculator;

    public AlertItem toAlert(
            TrafficEventItem item,
            double userLat,
            double userLon
    ) {
        double dist = distanceCalculator.calculate(userLat, userLon, item.getLat(), item.getLon());

        String msg = String.format(
                "전방 %d미터 앞 %s 상황입니다.",
                (int) dist,
                item.getEventDetailType()
        );

        return AlertItem.builder()
                .source(AlertItem.SourceType.TRAFFIC_EVENT)
                .distance(dist)
                .message(msg)
                .build();
    }

    public List<AlertItem> toAlerts(
            List<TrafficEventItem> items,
            double userLat,
            double userLon
    ) {
        return items.stream()
                .map(i -> toAlert(i, userLat, userLon))
                .toList();
    }
}