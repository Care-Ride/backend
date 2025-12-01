package backend.knowhow.domain.alert.mapper;

import backend.knowhow.domain.alert.dto.external.TrafficEventItem;
import backend.knowhow.domain.alert.dto.internal.AlertItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TrafficEventMapper {

    public AlertItem toAlert(
            TrafficEventItem item,
            double userLat,
            double userLon
    ) {
        double dist = distance(userLat, userLon, item.getLat(), item.getLon());

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

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371000; // meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);

        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }
}