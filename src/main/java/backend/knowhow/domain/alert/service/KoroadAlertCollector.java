package backend.knowhow.domain.alert.service;

import backend.knowhow.domain.alert.domain.KoroadHotspot;
import backend.knowhow.domain.alert.dto.internal.AlertItem;
import backend.knowhow.domain.alert.mapper.KoroadHotspotMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class KoroadAlertCollector {

    private final KoroadHotspotGeometryCache cache;
    private final KoroadHotspotMapper mapper;
    private final GeometryFactory geometryFactory = new GeometryFactory();

    // Koroad 사고 다발지역 기반 AlertItem 수집
    public List<AlertItem> collect(double userLat, double userLon, double rangeKm) {

        List<AlertItem> result = new ArrayList<>();

        double maxDistanceM = rangeKm * 1000.0;
        Point userPoint = geometryFactory.createPoint(new Coordinate(userLon, userLat));

        for (KoroadHotspotGeometryCache.CachedHotspot ch : cache.getHotspots()) {

            KoroadHotspot hotspot = ch.getEntity();

            // 1) 중심점 기준으로 반경 필터 (빠른 1차 필터)
            AlertItem alert = mapper.toAlert(hotspot, userLat, userLon);
            if (alert.getDistance() > maxDistanceM) {
                continue;
            }

            // 2) 폴리곤 내부/근접 여부 체크 (정확한 판정)
            //    "폴리곤 안에 들어가면"만 보내고 싶으면 이 조건 유지
            //    "폴리곤 근처여도 보내고 싶다"면 이 조건을 완화해도 됨
            if (!ch.getGeometry().contains(userPoint) &&
                    !ch.getGeometry().covers(userPoint)) {
                // 근처지만 폴리곤 밖이면, 정책에 따라:
                // - 근접도 알림: 이 if 블록 제거
                // - 엄밀히 내부만: 지금처럼 continue
                continue;
            }

            log.info("[KoroadAlertCollector] INSIDE polygon: hotspotId={}, name={}, dist={}m",
                    hotspot.getId(), hotspot.getSpotName(), alert.getDistance());

            result.add(alert);
        }

        log.info("[KoroadAlertCollector] collected {} koroad alerts", result.size());
        return result;
    }
}
