package backend.knowhow.domain.mission.domain;

import backend.knowhow.domain.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    private LocalDateTime completedAt; // 조건 달성 시점
    private LocalDateTime receivedAt;  // 포인트 수령 시점

    // 미션 달성
    public void complete() {
        if (this.status == MissionStatus.INCOMPLETE) {
            this.status = MissionStatus.COMPLETED;
            this.completedAt = LocalDateTime.now();
        }
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
