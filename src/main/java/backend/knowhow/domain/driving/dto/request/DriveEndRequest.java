package backend.knowhow.domain.driving.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DriveEndRequest {
    private Long driveId;
    private double totalDistance;
    private int hardAccelCount;
    private int hardDecelCount;
    private int suddenStopCount;
    private double lat;
    private double lon;
}
