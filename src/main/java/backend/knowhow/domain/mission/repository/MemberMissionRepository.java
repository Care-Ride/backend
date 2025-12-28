package backend.knowhow.domain.mission.repository;

import backend.knowhow.domain.member.domain.Member;
import backend.knowhow.domain.mission.domain.MemberMission;
import backend.knowhow.domain.mission.domain.Mission;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberMissionRepository extends JpaRepository<MemberMission, Long> {

    // 포인트 받을 때 락으로 잡고 상태 확인하고 변경
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select mm from MemberMission mm where mm.member = :member and mm.mission = :mission")
    Optional<MemberMission> findByMemberAndMissionForUpdate(Member member, Mission mission);

    Optional<MemberMission> findByMemberAndMission(Member member, Mission mission);

    @Query("select mm from MemberMission mm join fetch mm.mission where mm.member = :member")
    List<MemberMission> findAllByMemberWithMission(Member member);
}