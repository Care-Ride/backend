package backend.knowhow.domain.mission.repository;

import backend.knowhow.domain.member.domain.Member;
import backend.knowhow.domain.mission.domain.PointHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;


public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {
    // 월 범위로 조회
    Page<PointHistory> findAllByMemberAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(Member member, LocalDateTime start, LocalDateTime end, Pageable pageable);

    void deleteByMemberId(Long memberId);
}
