package backend.knowhow.domain.driving.dto.summary;

import backend.knowhow.domain.driving.domain.DrivingSession;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
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
    private String duration;

    private Double distance;        // 주행거리
    private int hardAccelCount;     // 급가속 횟수
    private int hardDecelCount;     // 급감속 횟수
    private int score;              // 운전 점수

    public static DrivingSessionSummary from(DrivingSession drive) {
        return DrivingSessionSummary.builder()
                .id(drive.getId())
                .driverId(drive.getDriver().getId())
                .startTime(drive.getStartTime())
                .endTime(drive.getEndTime())
                .duration(getDrivingDurationText(drive.getStartTime(), drive.getEndTime()))
                .distance(drive.getDistance())
                .hardAccelCount(drive.getHardAccelCount())
                .hardDecelCount(drive.getHardDecelCount())
                .score(drive.getScore())
                .build();
    }

    public static String getDrivingDurationText(LocalDateTime startTime, LocalDateTime endTime){
        if(startTime == null || endTime == null){
            return "0분";
        }

        Duration duration = Duration.between(startTime, endTime);
        long totalMinutes = duration.toMinutes();
        long hours = totalMinutes / 60;
        long minutes = totalMinutes % 60;

        return String.format("%02d시 %02d분", hours, minutes);
    }
}
