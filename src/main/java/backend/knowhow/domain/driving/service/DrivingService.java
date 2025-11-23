package backend.knowhow.domain.driving.service;

import backend.knowhow.domain.driving.domain.WeatherCondition;
import backend.knowhow.domain.driving.dto.request.BeforeDriveDangerRequest;
import backend.knowhow.domain.driving.dto.response.BeforeDriveDangerResponse;
import backend.knowhow.domain.driving.dto.response.KmaUltraSrtNcstResponse;
import backend.knowhow.domain.driving.service.weather.KmaWeatherClient;
import backend.knowhow.domain.driving.service.weather.WeatherConditionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class DrivingService {

    private final KmaWeatherClient kmaWeatherClient;
    private final WeatherConditionMapper weatherConditionMapper;

    private static final LocalTime NIGHT_START = LocalTime.of(20, 0);   // 20:00
    private static final LocalTime NIGHT_END = LocalTime.of(6, 0);  // 6:00

    @Transactional
    public BeforeDriveDangerResponse getDangerBeforeDrive(BeforeDriveDangerRequest request) {
        // 현재 위치 기준 현재 날씨 조회
        KmaUltraSrtNcstResponse ultraSrtNcst = kmaWeatherClient.getUltraSrtNcst(request.getLat(), request.getLon());
        WeatherCondition weatherCondition = weatherConditionMapper.fromUltraSrtNcst(ultraSrtNcst);  // 날씨 상태 enum값

        // 현재 시간 기준 야간운전인지 여부 확인
        LocalTime now = LocalTime.now();
        boolean isNightNow = now.isAfter(NIGHT_START) || now.isBefore(NIGHT_END);
        
        return new BeforeDriveDangerResponse(isNightNow, weatherCondition);
    }
}
