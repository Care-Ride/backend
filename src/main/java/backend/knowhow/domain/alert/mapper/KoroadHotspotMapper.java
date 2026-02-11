package backend.knowhow.domain.alert.mapper;

import backend.knowhow.domain.alert.domain.KoroadHotspot;
import backend.knowhow.domain.alert.dto.internal.AlertItem;
import backend.knowhow.domain.alert.util.DistanceCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KoroadHotspotMapper {

    private final DistanceCalculator distanceCalculator;

    public AlertItem toAlert(
            KoroadHotspot hotspot,
            double userLat,
            double userLon
    ) {
        double dist = distanceCalculator.calculate(
                userLat, userLon,
                hotspot.getLat(), hotspot.getLon()
        );

        int m = (int) Math.round(dist);

        String typeLabel = switch (hotspot.getType()) {
            case OLD_MAN -> "보행 노인 교통사고 다발지역";
            case CHILD -> "보행 어린이 교통사고 다발지역";
            case SCHOOL -> "어린이 보호구역 내 교통사고 다발지역";
            default -> "교통사고 다발지역";
        };

        // 최종 음성 안내 문구
        String msg = String.format(
                "전방 %d미터 앞 %s %s입니다.",
                m,
                hotspot.getSidoSggName() != null ? hotspot.getSidoSggName() : "",
                typeLabel
        );

        return AlertItem.builder()
                .source(AlertItem.SourceType.ACCIDENT_SPOT)
                .distance(dist)
                .message(msg)
                .build();
    }
}
