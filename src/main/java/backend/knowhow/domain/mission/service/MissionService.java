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
import org.springframework.dao.DataIntegrityViolationException;
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
    private final PointService pointService;

    @Transactional(readOnly = true)
    public List<MissionResponse> getMyMissions(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(ErrorType.MEMBER_NOT_FOUND));
        List<Mission> missions = missionRepository.findAll();
        List<MemberMission> memberMissions = memberMissionRepository.findAllByMemberWithMission(member);

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
        // 재조회 : 외부에서 넘어온 Member 객체는 영속성 컨텍스트에 없을 수 있으므로(detached 상태) managed entity 보장하기 위함
        Member managedMember = memberRepository.findById(member.getId())
                .orElseThrow(() -> new BaseException(ErrorType.MEMBER_NOT_FOUND));


        Mission mission = missionRepository.findByCode(missionCode)
                .orElseThrow(() -> new BaseException(ErrorType.MISSION_NOT_FOUND));

        MemberMission memberMission =
                memberMissionRepository.findByMemberAndMission(managedMember, mission)
                        .orElseGet(() ->
                                memberMissionRepository.save(
                                        MemberMission.createNew(managedMember, mission)
                                )
                        );

        // 이미 포인트 받은 미션이면 종료
        if (memberMission.getStatus() == MissionStatus.RECEIVED) {
            return;
        }
        memberMission.complete();
    }

    @Transactional
    public void claimPoint(Long memberId, MissionCode missionCode) {
        // Member를 락으로 읽어서 동시성 손실 방지
        Member member = memberRepository.findByIdForUpdate(memberId)
                .orElseThrow(() -> new BaseException(ErrorType.MEMBER_NOT_FOUND));
        Mission mission = missionRepository.findByCode(missionCode)
                .orElseThrow(() -> new BaseException(ErrorType.MISSION_NOT_FOUND));
        MemberMission memberMission =
                memberMissionRepository.findByMemberAndMissionForUpdate(member, mission)
                        .orElseThrow(() -> new BaseException(ErrorType.MISSION_NOT_COMPLETED));

        if (memberMission.getStatus() == MissionStatus.INCOMPLETE) {
            throw new BaseException(ErrorType.MISSION_NOT_COMPLETED);
        }

        if (memberMission.getStatus() == MissionStatus.RECEIVED) {
            throw new BaseException(ErrorType.MISSION_ALREADY_RECEIVED);
        }
        // COMPLETED -> RECEIVED
        memberMission.receive();

        // 포인트 지급 및 내역 생성
        pointService.earnMissionReward(member, mission.getRewardPoint(), mission.getTitle());
    }
}