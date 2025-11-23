package backend.knowhow.domain.driving.dto.response;

import backend.knowhow.domain.driving.domain.WeatherCondition;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BeforeDriveDangerResponse {
    private Boolean isNightDrive;
    private WeatherCondition weather;
}
