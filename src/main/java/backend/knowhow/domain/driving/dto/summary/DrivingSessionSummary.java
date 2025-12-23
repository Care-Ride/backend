package backend.knowhow.domain.driving.dto.summary;

import backend.knowhow.domain.driving.domain.DrivingSession;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor()
@Builder
public class DrivingSessionSummary {

    private Long id;
    private Long driverId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private Double distance;        // 주행거리
    private int hardAccelCount;     // 급가속 횟수
    private int hardDecelCount;     // 급감속 횟수
    private int score;              // 운전 점수

    private double startLat;
    private double startLon;
    private double endLat;
    private double endLon;

    public static DrivingSessionSummary from(DrivingSession drive) {
        return DrivingSessionSummary.builder()
                .id(drive.getId())
                .driverId(drive.getDriver().getId())
                .startTime(drive.getStartTime())
                .endTime(drive.getEndTime())
                .distance(drive.getDistance())
                .hardAccelCount(drive.getHardAccelCount())
                .hardDecelCount(drive.getHardDecelCount())
                .score(drive.getScore())
                .startLat(drive.getStartLat())
                .startLon(drive.getStartLon())
                .endLat(drive.getEndLat())
                .endLon(drive.getEndLon())
                .build();
    }
}
