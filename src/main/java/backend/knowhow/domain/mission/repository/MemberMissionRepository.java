package backend.knowhow.domain.mission.repository;

import backend.knowhow.domain.member.domain.Member;
import backend.knowhow.domain.mission.domain.MemberMission;
import backend.knowhow.domain.mission.domain.Mission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberMissionRepository extends JpaRepository<MemberMission, Long> {

    Optional<MemberMission> findByMemberAndMission(Member member, Mission mission);

    List<MemberMission> findAllByMember(Member member);
}
