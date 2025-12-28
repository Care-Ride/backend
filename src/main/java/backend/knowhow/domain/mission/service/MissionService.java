package backend.knowhow.domain.mission.service;

import backend.knowhow.domain.member.domain.Member;
import backend.knowhow.domain.member.repository.MemberRepository;
import backend.knowhow.domain.mission.domain.*;
import backend.knowhow.domain.mission.dto.MissionResponse;
import backend.knowhow.domain.mission.repository.MemberMissionRepository;
import backend.knowhow.domain.mission.repository.MissionRepository;
import backend.knowhow.global.common.exception.BaseException;
import backend.knowhow.global.common.response.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MissionService {

    private final MemberRepository memberRepository;
    private final MissionRepository missionRepository;
    private final MemberMissionRepository memberMissionRepository;

    @Transactional(readOnly = true)
    public List<MissionResponse> getMyMissions(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(ErrorType.MEMBER_NOT_FOUND));
        List<Mission> missions = missionRepository.findAll();
        List<MemberMission> memberMissions = memberMissionRepository.findAllByMember(member);

        Map<MissionCode, MemberMission> map = memberMissions.stream()
                .collect(Collectors.toMap(
                        memberMission -> memberMission.getMission().getCode(),
                        memberMission -> memberMission
                ));

        return missions.stream()
                .map(mission -> MissionResponse.from(mission, map.get(mission.getCode())))
                .toList();
    }

    @Transactional
    public void completeMission(Member member, MissionCode missionCode) {
        Mission mission = missionRepository.findByCode(missionCode)
                .orElseThrow(() -> new BaseException(ErrorType.MISSION_NOT_FOUND));

        MemberMission memberMission = memberMissionRepository.findByMemberAndMission(member, mission)
                .orElseGet(() -> memberMissionRepository.save(MemberMission.createNew(member, mission)));

        // 이미 포인트 받은 미션이면 종료
        if (memberMission.getStatus() == MissionStatus.RECEIVED) {
            return;
        }
        memberMission.complete();
    }

    @Transactional
    public void claimPoint(Long memberId, MissionCode missionCode) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(ErrorType.MEMBER_NOT_FOUND));
        Mission mission = missionRepository.findByCode(missionCode)
                .orElseThrow(() -> new IllegalArgumentException("미션 없음"));
        MemberMission memberMission = memberMissionRepository.findByMemberAndMission(member, mission)
                .orElseThrow(() -> new IllegalStateException("미션 수행 기록이 없습니다."));

        if (memberMission.getStatus() == MissionStatus.INCOMPLETE) {
            throw new BaseException(ErrorType.MISSION_NOT_COMPLETED);
        }

        if (memberMission.getStatus() == MissionStatus.RECEIVED) {
            throw new BaseException(ErrorType.MISSION_ALREADY_RECEIVED);
        }
        // COMPLETED -> RECEIVED
        memberMission.receive();

    }
}