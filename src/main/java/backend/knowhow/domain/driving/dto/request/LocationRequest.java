package backend.knowhow.domain.driving.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LocationRequest {
    private double lat;
    private double lon;
}
