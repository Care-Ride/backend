package backend.knowhow.domain.mission.domain;

import backend.knowhow.domain.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member_mission", uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_member_mission",
                        columnNames = {"member_id", "mission_id"}
                )
        }
)
public class MemberMission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mission_id", nullable = false)
    private Mission mission;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MissionStatus status;

    // 운전 챌린지 미션일 경우 마지막으로 평가된 운전 세션 ID
    private Long lastEvaluatedDrivingSessionId;

    private LocalDateTime completedAt; // 조건 달성 시점
    private LocalDateTime receivedAt;  // 포인트 수령 시점

    // 도전과제형(1회성) 미션 달성
    public void completeAchievement() {
        if (this.status == MissionStatus.INCOMPLETE) {
            this.status = MissionStatus.COMPLETED;
            this.completedAt = LocalDateTime.now();
        }
    }

    // 챌린지형 미션 달성
    public void completeChallenge(Long drivingSessionId) {
        // 같은 주행세션 중복 평가 방지
        if (drivingSessionId != null && drivingSessionId.equals(this.lastEvaluatedDrivingSessionId)) {
            return;
        }
        this.status = MissionStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
        this.lastEvaluatedDrivingSessionId = drivingSessionId;
    }

    // 챌린지형 미션 초기화
    public void resetChallenge() {
        if (this.status == MissionStatus.INCOMPLETE) return;
        this.status = MissionStatus.INCOMPLETE;
        this.completedAt = null;
        this.receivedAt = null;
        this.lastEvaluatedDrivingSessionId = null;
    }

    // 포인트 수령
    public void receive() {
        if (this.status == MissionStatus.COMPLETED) {
            this.status = MissionStatus.RECEIVED;
            this.receivedAt = LocalDateTime.now();
        }
    }

    @Builder(access = AccessLevel.PRIVATE)
    private MemberMission(Member member, Mission mission, MissionStatus missionStatus){
        this.member = member;
        this.mission = mission;
        this.status = missionStatus;
    }

    public static MemberMission createNew(Member member, Mission mission){
        return MemberMission.builder()
                .member(member)
                .mission(mission)
                .missionStatus(MissionStatus.INCOMPLETE)
                .build();

    }
}
