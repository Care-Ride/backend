package backend.knowhow.domain.driving.service.kakaoMap;

import backend.knowhow.domain.driving.dto.response.kakao.KakaoPlaceSearchResponse;
import backend.knowhow.global.common.exception.BaseException;
import backend.knowhow.global.common.response.ErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoApiClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${kakao.map.kakao-api-key}")
    private String kakaoApiKey;

    private static final String KEYWORD_API_URL = "https://dapi.kakao.com/v2/local/search/keyword.json";
    private static final int KEYWORD_LIST_SIZE = 10;

    // 키워드로 장소 검색
    public KakaoPlaceSearchResponse searchByKeyword(String keyword) {

        URI uri = UriComponentsBuilder
                .fromHttpUrl(KEYWORD_API_URL)
                .queryParam("query", keyword)
                .queryParam("page", 1)
                .queryParam("size", KEYWORD_LIST_SIZE)
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUri();

        // Header 설정
        HttpEntity<String> entity = createAuthHeaders();

        try {
            ResponseEntity<KakaoPlaceSearchResponse> response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    entity,
                    KakaoPlaceSearchResponse.class
            );

            if (response.getBody() == null) {
                throw new BaseException(ErrorType.KAKAO_MAP_ERROR);
            }
            return response.getBody();
        } catch (Exception e) {
            log.error("[KakaoApiClient] searchByKeyword error. keyword={}, message={}", keyword, e.getMessage(), e);
            throw new BaseException(ErrorType.KAKAO_MAP_ERROR);
        }
    }

    private HttpEntity<String> createAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoApiKey);
        return new HttpEntity<>(headers);
    }
}
