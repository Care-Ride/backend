package backend.knowhow.domain.alert.mapper;

import backend.knowhow.domain.alert.dto.external.CautionSectionItem;
import backend.knowhow.domain.alert.dto.internal.AlertItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CautionSectionMapper {

    public AlertItem toAlert(
            CautionSectionItem item,
            double userLat,
            double userLon
    ) {
        // 정방향만 사용 -> 추후 링크 방향성 처리
        double dist = distance(userLat, userLon, item.getStartY(), item.getStartX());

        return AlertItem.builder()
                .source(AlertItem.SourceType.CAUTION_SECTION)
                .distance(dist)
                .message(buildMessage(item, dist))
                .build();
    }

    public List<AlertItem> toAlerts(
            List<CautionSectionItem> items,
            double userLat,
            double userLon
    ) {
        return items.stream()
                .map(i -> toAlert(i, userLat, userLon))
                .toList();
    }

    private String buildMessage(CautionSectionItem item, double dist) {
        int m = (int) Math.round(dist);
        String type = item.getOutbrkType() != null ? item.getOutbrkType() : "주의구간";

        return String.format("전방 %d미터 앞 %s입니다.", m, type);
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
