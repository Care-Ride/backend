package backend.knowhow.domain.mobility.service;

import backend.knowhow.domain.mobility.dto.RegionInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReverseGeoApiClient {

    private final RestTemplate restTemplate;

    @Value("${naver.maps.client-id}")
    private String clientId;

    @Value("${naver.maps.client-secret}")
    private String clientSecret;

    private static final String NAVER_GEOCODE_URL = "https://maps.apigw.ntruss.com/map-reversegeocode/v2/gc";

    public Optional<RegionInfo> findRegionInfo(double lat, double lon) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-NCP-APIGW-API-KEY-ID", clientId);
        headers.set("X-NCP-APIGW-API-KEY", clientSecret);

        String url = UriComponentsBuilder.fromHttpUrl(NAVER_GEOCODE_URL)
                .queryParam("coords", lon + "," + lat)
                .queryParam("output", "json")
                .queryParam("orders", "admcode")
                .toUriString();

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    url, HttpMethod.GET, new HttpEntity<>(headers), Map.class
            );

            Map<String, Object> body = response.getBody();
            if (body == null || body.get("results") == null) {
                return Optional.empty();
            }
            List<?> results = (List<?>) body.get("results");
            if (results.isEmpty()) {
                return Optional.empty();
            }
            Map<?, ?> firstResult = (Map<?, ?>) results.get(0);
            Map<?, ?> region = (Map<?, ?>) firstResult.get("region");

            if (region == null) {
                return Optional.empty();
            }

            Map<?, ?> area1 = (Map<?, ?>) region.get("area1");
            Map<?, ?> area2 = (Map<?, ?>) region.get("area2");
            Map<?, ?> area3 = (Map<?, ?>) region.get("area3");
            if (area1 == null) {
                return Optional.empty();
            }
            String city = area1 != null ? (String) area1.get("name") : null;
            String district = area2 != null ? (String) area2.get("name") : null;
            String dong = area3 != null ? (String) area3.get("name") : null;

            if (city == null) return Optional.empty();

            return Optional.of(new RegionInfo(city, district, dong));

        } catch (HttpClientErrorException e) {
            log.error("[NaverMapClient] HTTP Error: status={}, body={}, lat={}, lon={}",
                    e.getStatusCode(), e.getResponseBodyAsString(), lat, lon);
            return Optional.empty();
        }
        catch (Exception e) {
            log.error("[NaverMapClient] 기타 에러: lat={}, lon={}, msg={}", lat, lon, e.getMessage(), e);
            return Optional.empty();
        }
    }
}
