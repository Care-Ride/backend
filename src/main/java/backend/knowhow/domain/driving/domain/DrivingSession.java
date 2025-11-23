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

    @ColumnDefault("0.0")
    private Double distance;    // 주행거리

    @ColumnDefault("0")
    private int hardAccelCount;    // 급가속 횟수

    @ColumnDefault("0")
    private int hardDecelCount;    // 급감속 횟수

    @ColumnDefault("0")
    private int suddenStopCount;    // 급제동 횟수

    private int score;    // 운전 점수

    // TODO: 논의 후 String 주소로 넣을 수도 있음.
    private double startLat;
    private double startLon;
    private double endLat;
    private double endLon;

    @Builder
    private DrivingSession(Member driver,
                           LocalDateTime startTime,
                           double startLat,
                           double startLon) {
        this.driver = driver;
        this.startTime = startTime;
        this.startLat = startLat;
        this.startLon = startLon;
    }

    public static DrivingSession start(Member driver, double startLat, double startLon) {
        return DrivingSession.builder()
                .driver(driver)
                .startTime(LocalDateTime.now())
                .startLat(startLat)
                .startLon(startLon)
                .build();
    }

    public void finish(double totalDistance,
                       int hardAccelCount,
                       int hardDecelCount,
                       int suddenStopCount,
                       double endLat,
                       double endLon,
                       int score) {
        this.endTime = LocalDateTime.now();
        this.distance = totalDistance;
        this.hardAccelCount = hardAccelCount;
        this.hardDecelCount = hardDecelCount;
        this.suddenStopCount = suddenStopCount;
        this.endLat = endLat;
        this.endLon = endLon;
        this.score = score;
    }
}
