package backend.knowhow.domain.alert.client;

import backend.knowhow.domain.alert.dto.external.KoroadBaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class KoroadApiClient {

    private final RestTemplate restTemplate;

    @Value("${koroad.api-key}")
    private String apiKey;

    @Value("${koroad.base-url}")
    private String baseUrl;

    private static final int searchYear = 2015;  // 최근 3년 기준 연도
    private static final String PATH_OLDMAN = "/frequentzoneOldman/getRestFrequentzoneOldman";
    private static final String PATH_CHILD = "/frequentzoneChild/getRestFrequentzoneChild";
    private static final String PATH_SCHOOL = "/frequentzoneChildSchool/getRestFrequentzoneChildSchool";

    // API 선택 → path 반환
    private String resolvePath(String type) {
        return switch (type.toLowerCase()) {
            case "oldman" -> PATH_OLDMAN;
            case "child" -> PATH_CHILD;
            case "school" -> PATH_SCHOOL;
            default -> throw new IllegalArgumentException("Unknown Koroad type: " + type);
        };
    }

    // Koroad API 호출 공통 함수
    public List<KoroadBaseResponse.Item> fetchHotspots(
            String type,   // "oldman", "child", "school"
            Integer siDo,
            Integer guGun
    ) {
        String path = resolvePath(type);

        String url = baseUrl + path
                + "?serviceKey=" + apiKey
                + "&searchYearCd=" + searchYear
                + "&siDo=" + siDo
                + "&guGun=" + guGun
                + "&type=json"
                + "&numOfRows=9999"
                + "&pageNo=1";

        try {
            KoroadBaseResponse res = restTemplate.getForObject(url, KoroadBaseResponse.class);
            if (res == null) {
                log.warn("[KoroadApiClient] Null response. url={}", url);
                return Collections.emptyList();
            }

            if (!"00".equals(res.getResultCode())) {
                log.warn(
                        "[KoroadApiClient] API error. url={}, code={}, msg={}",
                        url, res.getResultCode(), res.getResultMsg()
                );
                return Collections.emptyList();
            }

            List<KoroadBaseResponse.Item> items = res.getItemList();
            log.info("[KoroadApiClient] success. type={}, siDo={}, guGun={}, itemCount={}",
                    type, siDo, guGun, items.size());

            return items;
        }
        catch (Exception e) {
            log.error("[KoroadApiClient] fetchHotspots error. type={}, url={}, message={}", type, url, e.getMessage(), e);
            return Collections.emptyList();
        }
    }
}
