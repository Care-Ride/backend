package backend.knowhow.domain.driving.domain;

import backend.knowhow.domain.member.domain.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "driving_session")
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class DrivingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member driver;

    @Column(nullable = false)
    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Double distance;    // 주행거리

    @ColumnDefault("0")
    private int hardAccelCount;    // 급가속 횟수

    @ColumnDefault("0")
    private int hardDecelCount;    // 급감속 횟수

    @ColumnDefault("0")
    private int suddenStopCount;    // 급제동 횟수

    private int score;    // 운전 점수

    private String startLocation;
    private String endLocation;

    @Builder
    private DrivingSession(Member driver,
                           LocalDateTime startTime,
                           Double distance,
                           int hardAccelCount,
                           int hardDecelCount,
                           int suddenStopCount,
                           int score,
                           String startLocation,
                           String endLocation) {

        this.driver = driver;
        this.startTime = startTime;
        this.distance = distance;
        this.hardAccelCount = hardAccelCount;
        this.hardDecelCount = hardDecelCount;
        this.suddenStopCount = suddenStopCount;
        this.score = score;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
    }

    public static DrivingSession start(Member driver, String startLocation, String endLocation) {
        return DrivingSession.builder()
                .driver(driver)
                .startTime(LocalDateTime.now())
                .startLocation(startLocation)
                .endLocation(endLocation)
                .build();
    }

    public void finish(double totalDistance,
                       int hardAccelCount,
                       int hardDecelCount,
                       int suddenStopCount,
                       String endLocation,
                       int score) {
        this.endTime = LocalDateTime.now();
        this.distance = totalDistance;
        this.hardAccelCount = hardAccelCount;
        this.hardDecelCount = hardDecelCount;
        this.suddenStopCount = suddenStopCount;
        this.endLocation = endLocation;
        this.score = score;
    }
}
