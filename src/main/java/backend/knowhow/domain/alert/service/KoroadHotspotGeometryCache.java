package backend.knowhow.domain.alert.service;

import backend.knowhow.domain.alert.domain.KoroadHotspot;
import backend.knowhow.domain.alert.repository.KoroadHotspotRepository;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.io.geojson.GeoJsonReader;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class KoroadHotspotGeometryCache {

    private final KoroadHotspotRepository hotspotRepository;

    private final GeometryFactory geometryFactory = new GeometryFactory();
    private final GeoJsonReader geoJsonReader = new GeoJsonReader(geometryFactory);

    @Getter
    private final List<CachedHotspot> hotspots = new ArrayList<>();

    @PostConstruct
    public void loadFromDb() {
        List<KoroadHotspot> all = hotspotRepository.findAll();
        int success = 0;

        for (KoroadHotspot entity : all) {
            String geomJson = entity.getGeomJson();
            if (geomJson == null || geomJson.isBlank()) {
                continue;
            }

            try {
                Geometry geom = geoJsonReader.read(geomJson); // GeoJSON → Geometry
                hotspots.add(new CachedHotspot(entity, geom));
                success++;
            } catch (Exception e) {
                log.warn("[KoroadHotspotGeometryCache] parse error id={}, msg={}",
                        entity.getId(), e.getMessage());
            }
        }

        log.info("[KoroadHotspotGeometryCache] loaded {} entities, {} geometries parsed.",
                all.size(), success);
    }

    @Getter
    public static class CachedHotspot {
        private final KoroadHotspot entity;
        private final Geometry geometry;

        public CachedHotspot(KoroadHotspot entity, Geometry geometry) {
            this.entity = entity;
            this.geometry = geometry;
        }
    }
}
