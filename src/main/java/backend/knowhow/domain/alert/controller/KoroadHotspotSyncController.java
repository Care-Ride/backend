package backend.knowhow.domain.alert.controller;

import backend.knowhow.domain.alert.service.KoroadHotspotSyncService;
import backend.knowhow.domain.alert.util.KoroadRegion;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/koroad")
public class KoroadHotspotSyncController {

    private final KoroadHotspotSyncService syncService;

    /**
     * 서울 전체 Koroad 다발지역 동기화
     *
     * 예)
     *  - 보행노인 사고 다발지역 전체: type=oldman
     *  - 보행어린이: type=child
     *  - 어린이보호구역: type=school
     */
    @PostMapping("/sync")
    public String syncSeoul(
            @RequestParam String type  // KoroadApiClient 에서 사용하는 구분자 ("oldman", "child", "school" 등)
    ) {

        log.info("sync start");
        int total = 0;

        for (var entry : KoroadRegion.SIDO_GUGUN.entrySet()) {
            int siDo = entry.getKey();
            for (int guGun : entry.getValue()) {
                syncService.syncHotspots(type, siDo, guGun);
                total++;
            }
        }

        return String.format("Synced Koroad hotspots for type=%s, gugunCount=%d",
                type, total);
    }
}

