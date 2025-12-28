package backend.knowhow.domain.mission.repository;

import backend.knowhow.domain.member.domain.Member;
import backend.knowhow.domain.mission.domain.PointHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {
    Page<PointHistory> findAllByMember(Member member, Pageable pageable);
}
