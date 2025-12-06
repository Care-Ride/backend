package backend.knowhow.domain.alert.service;

import backend.knowhow.domain.alert.client.KoroadApiClient;
import backend.knowhow.domain.alert.domain.KoroadHotspot;
import backend.knowhow.domain.alert.dto.external.KoroadBaseResponse;
import backend.knowhow.domain.alert.repository.KoroadHotspotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class KoroadHotspotSyncService {

    private final KoroadApiClient koroadApiClient;
    private final KoroadHotspotRepository hotspotRepository;

    // Koroad API → MySQL 저장 (해당 type + 시/도 + 구/군 기준으로 전체 갱신)
    @Transactional
    public void syncHotspots(String apiType, String type, Integer siDo, Integer guGun) {
        // Koroad에서 신규 데이터 가져오기
        List<KoroadBaseResponse.Item> items = koroadApiClient.fetchHotspots(apiType, siDo, guGun);

        if (items.isEmpty()) {
            log.warn("[KoroadHotspotSyncService] No items fetched. Skipping sync for type={}, siDo={}, guGun={}", type, siDo, guGun);
            return;
        }

        // 기존 데이터 삭제 (같은 type + region 기준)
        hotspotRepository.deleteByTypeAndSiDoAndGuGun(type, siDo, guGun);

        // 3) 새 데이터 저장
        List<KoroadHotspot> entities = items.stream()
                .map(item -> {
                    Double lat = null;
                    Double lon = null;

                    try {
                        if (item.getLaCrd() != null)
                            lat = Double.parseDouble(item.getLaCrd());
                        if (item.getLoCrd() != null)
                            lon = Double.parseDouble(item.getLoCrd());
                    } catch (NumberFormatException e) {
                        log.warn("[KoroadHotspotSyncService] invalid coord la={}, lo={}",
                                item.getLaCrd(), item.getLoCrd());
                    }

                    return KoroadHotspot.builder()
                            .type(type)
                            .afosFid(item.getAfosId())
                            .spotName(item.getSpotName())
                            .sidoSggName(item.getSidoSggName())
                            .siDo(siDo)
                            .guGun(guGun)
                            .accidentCount(item.getAccidentCount())
                            .lat(lat)
                            .lon(lon)
                            .geomJson(item.getGeomJson())
                            .build();
                })
                .toList();

        hotspotRepository.saveAll(entities);
    }
}
