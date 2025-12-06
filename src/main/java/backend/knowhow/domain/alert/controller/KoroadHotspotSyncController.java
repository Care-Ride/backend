package backend.knowhow.domain.alert.controller;

import backend.knowhow.domain.alert.service.KoroadHotspotSyncService;
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

    // 서울 시도 코드
    private static final int SEOUL_SIDO = 11;

    // 서울 전체 구/군 코드 목록 (Koroad 기준)
    private static final List<Integer> SEOUL_GUGUN_CODES = List.of(
            110, // 종로구
            140, // 중구
            170, // 용산구
            200, // 성동구
            215, // 광진구
            230, // 동대문구
            260, // 중랑구
            290, // 성북구
            305, // 강북구
            320, // 도봉구
            350, // 노원구
            380, // 은평구
            410, // 서대문구
            440, // 마포구
            470, // 양천구
            500, // 강서구
            530, // 구로구
            545, // 금천구
            560, // 영등포구
            590, // 동작구
            620, // 관악구
            650, // 서초구
            680, // 강남구
            710, // 송파구
            740  // 강동구
    );

    /**
     * 서울 전체 Koroad 다발지역 동기화
     *
     * 예)
     *  - 보행노인 사고 다발지역 전체: apiType=oldman&type=OLD_MAN
     *  - 보행어린이: apiType=child&type=CHILD
     *  - 어린이보호구역: apiType=school&type=SCHOOL
     */
    @PostMapping("/sync/seoul")
    public String syncSeoul(
            @RequestParam String apiType,  // KoroadApiClient 에서 사용하는 구분자 ("oldman", "child", "school" 등)
            @RequestParam String type      // DB KoroadHotspot.type 에 저장할 값 ("OLD_MAN", "CHILD", "SCHOOL" 등)
    ) {

        log.info("start");
        int total = 0;

        for (Integer guGun : SEOUL_GUGUN_CODES) {
            syncService.syncHotspots(apiType, type, SEOUL_SIDO, guGun);
            log.info("{} finish", guGun);
            total++;
        }

        return String.format("Synced Seoul Koroad hotspots for apiType=%s, type=%s, gugunCount=%d",
                apiType, type, total);
    }
}

