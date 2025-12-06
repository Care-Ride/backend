//package backend.knowhow.domain.alert.service;
//
//import backend.knowhow.domain.alert.domain.KoroadHotspot;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.locationtech.jts.geom.Coordinate;
//import org.locationtech.jts.geom.GeometryFactory;
//import org.locationtech.jts.geom.Point;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Service
//@Slf4j
//@RequiredArgsConstructor
//public class KoroadRiskService {
//
//    private final KoroadHotspotGeometryCache cache;
//    private final GeometryFactory geometryFactory = new GeometryFactory();
//
//    // 현재 위치가 Koroad hotspot 폴리곤 안에 들어가는지 여부
//    public Optional<KoroadHotspot> findContaining(double lat, double lon) {
//        Point point = geometryFactory.createPoint(new Coordinate(lon, lat)); // x=lon, y=lat
//
//        for (KoroadHotspotGeometryCache.CachedHotspot ch : cache.getHotspots()) {
//            if (ch.getGeometry().contains(point) || ch.getGeometry().covers(point)) {
//                log.info("inside risk!!!!");
//                return Optional.of(ch.getEntity());
//            }
//        }
//        return Optional.empty();
//    }
//
//    public boolean isInside(double lat, double lon) {
//        return findContaining(lat, lon).isPresent();
//    }
//}
//
