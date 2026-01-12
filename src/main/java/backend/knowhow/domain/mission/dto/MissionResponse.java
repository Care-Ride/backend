package backend.knowhow.domain.mission.dto;

import backend.knowhow.domain.mission.domain.MemberMission;
import backend.knowhow.domain.mission.domain.Mission;
import backend.knowhow.domain.mission.domain.MissionCode;
import backend.knowhow.domain.mission.domain.MissionStatus;

import java.time.LocalDateTime;

public record MissionResponse(
        MissionCode missionCode,
        String title,
        String detail,
        int rewardPoint,
        MissionStatus status,
        LocalDateTime completedAt
) {
    public static MissionResponse from(Mission mission, MemberMission memberMission) {
        if (memberMission == null) {
            return new MissionResponse(
                    mission.getCode(),
                    mission.getTitle(),
                    mission.getDetail(),
                    mission.getRewardPoint(),
                    MissionStatus.INCOMPLETE,
                    null
            );
        }

        return new MissionResponse(
                mission.getCode(),
                mission.getTitle(),
                mission.getDetail(),
                mission.getRewardPoint(),
                memberMission.getStatus(),
                memberMission.getCompletedAt()
        );
    }
}
