package backend.knowhow.domain.driving.service.weather;

import backend.knowhow.domain.driving.domain.WeatherCondition;
import backend.knowhow.domain.driving.dto.response.weather.KmaUltraSrtFcstResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class WeatherConditionMapper {
    
    // ultraSrtNcst 응답 전체에서 날씨 정보 뽑아서 최종 ENUM 반환
    public WeatherCondition fromUltraSrtFcst(KmaUltraSrtFcstResponse res) {
        if (res == null
                || res.getResponse() == null
                || res.getResponse().getBody() == null
                || res.getResponse().getBody().getItems() == null
                || res.getResponse().getBody().getItems().getItem() == null) {
            log.warn("[Weather] KMA response is empty, res={}", res);
            return WeatherCondition.UNKNOWN;
        }
        List<KmaUltraSrtFcstResponse.Item> items = res.getResponse().getBody().getItems().getItem();

        String pty = extractCategory(items, "PTY");  // 강수형태
        String sky = extractCategory(items, "SKY");  // 맑음/흐림 정보

        log.info("pty = {}, sky = {}", pty, sky);

        return fromKmaCodes(pty, sky);
    }

    // 기상청 코드(PTY, SKY) -> ENUM 매핑
    private WeatherCondition fromKmaCodes(String pty, String sky) {
        // 강수형태(PTY)
        if (pty != null) {
            switch (pty) {
                case "0": return WeatherCondition.SUNNY;        // 강수 없는 경우
                case "1": return WeatherCondition.RAIN;         // 비
                case "2": return WeatherCondition.RAIN_SNOW;    // 비/눈
                case "3": return WeatherCondition.SNOW;         // 눈
                case "4": return WeatherCondition.SHOWER;       // 소나기
            }
        }

        // 강수가 없으면 SKY로
        if (sky != null) {
            switch (sky) {
                case "1": return WeatherCondition.SUNNY;      // 맑음
                case "4": return WeatherCondition.OVERCAST;   // 흐림
            }
        }

        return WeatherCondition.UNKNOWN;
    }

    // 공통 카테고리 추출 로직 (PTY, SKY값 추출 용도)
    private String extractCategory(List<KmaUltraSrtFcstResponse.Item> items, String category) {
        return items.stream()
                .filter(i -> category.equals(i.getCategory()))
                .findFirst()
                .map(KmaUltraSrtFcstResponse.Item::getFcstValue)
                .orElse(null);
    }

}
