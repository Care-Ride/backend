package backend.knowhow.domain.driving.domain;

import backend.knowhow.domain.member.domain.Member;
import backend.knowhow.domain.vehicle.domain.Vehicle;
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
@Table(
        name = "driving_session",
        indexes = {
                @Index(name = "idx_driving_session_member_start_time", columnList = "member_id, start_time")
        }
)
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class DrivingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member driver;

    // 사용한 차량(nullable)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @Column(nullable = false)
    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @ColumnDefault("0.0")
    private Double distance;    // 주행거리

    @ColumnDefault("0")
    private int hardAccelCount;    // 급가속 횟수

    @ColumnDefault("0")
    private int hardDecelCount;    // 급감속 횟수

    private int score;    // 운전 점수

    @Column(nullable = false)
    private boolean pointEligible;  // 포인트 적립 대상 여부(블루투스 연결 기준)

    // TODO: 논의 후 String 주소로 넣을 수도 있음.
    private double startLat;
    private double startLon;
    private double endLat;
    private double endLon;

    @Builder
    private DrivingSession(Member driver,
                           Vehicle vehicle,
                           LocalDateTime startTime,
                           double startLat,
                           double startLon,
                           boolean pointEligible) {
        this.driver = driver;
        this.vehicle = vehicle;
        this.startTime = startTime;
        this.startLat = startLat;
        this.startLon = startLon;
        this.pointEligible = pointEligible;
    }

    public static DrivingSession start(Member driver, Vehicle vehicle, double startLat, double startLon, boolean pointEligible) {
        return DrivingSession.builder()
                .driver(driver)
                .vehicle(vehicle)
                .startTime(LocalDateTime.now())
                .startLat(startLat)
                .startLon(startLon)
                .pointEligible(pointEligible)
                .build();
    }

    public void finish(double totalDistance,
                       int hardAccelCount,
                       int hardDecelCount,
                       double endLat,
                       double endLon,
                       int score) {
        this.endTime = LocalDateTime.now();
        this.distance = totalDistance;
        this.hardAccelCount = hardAccelCount;
        this.hardDecelCount = hardDecelCount;
        this.endLat = endLat;
        this.endLon = endLon;
        this.score = score;
    }
}
