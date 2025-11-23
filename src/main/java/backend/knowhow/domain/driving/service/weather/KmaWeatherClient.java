package backend.knowhow.domain.driving.service.weather;

import backend.knowhow.domain.driving.dto.response.weather.KmaUltraSrtNcstResponse;
import backend.knowhow.global.common.exception.BaseException;
import backend.knowhow.global.common.response.ErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
@Slf4j
public class KmaWeatherClient {

    private final GpsUtils gpsUtils;
    private final RestTemplate restTemplate;

    @Value("${kma.weather.base-url}")
    private String baseUrl;

    @Value("${kma.weather.service-key}")
    private String serviceKey;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HHmm");

    public KmaUltraSrtNcstResponse getUltraSrtNcst(double lat, double lan) {
        // 위도경도 -> 기상청 격자로 반환
        GpsUtils.LatXLngY latXLngY = gpsUtils.convertGRID_GPS(GpsUtils.TO_GRID, lat, lan);

        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

        // 발표시각 근사: 40분 기준 고려해서 직전 정시로 맞추기
        // (예: 10:10 -> 09:00, 10:50 -> 10:00)
        LocalDateTime baseDateTime = now.minusMinutes(40);
        String baseDate = baseDateTime.format(DATE_FMT);
        String baseTime = String.format("%02d00", baseDateTime.getHour());

        String url = UriComponentsBuilder
                .fromHttpUrl(baseUrl + "/getUltraSrtNcst")
                .queryParam("serviceKey", serviceKey)
                .queryParam("pageNo", 1)
                .queryParam("numOfRows", 1000)
                .queryParam("dataType", "JSON")
                .queryParam("base_date", baseDate)
                .queryParam("base_time", baseTime)
                .queryParam("nx", (int) latXLngY.nx)
                .queryParam("ny", (int) latXLngY.ny)
                .toUriString();

        try {
            return restTemplate.getForObject(url, KmaUltraSrtNcstResponse.class);
        } catch (Exception e) {
            throw new BaseException(ErrorType.KMA_WEATHER_ERROR);
        }
    }
}
