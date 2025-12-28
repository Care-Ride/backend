package backend.knowhow.domain.mission.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Mission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private int rewardPoint;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private MissionCode code;

    @Column(nullable = false, length = 500)
    private String detail;

    public static Mission createMission(String title, int rewardPoint, MissionCode code, String detail) {
        Mission mission = new Mission();
        mission.title = title;
        mission.rewardPoint = rewardPoint;
        mission.code = code;
        mission.detail = detail;
        return mission;
    }


}
