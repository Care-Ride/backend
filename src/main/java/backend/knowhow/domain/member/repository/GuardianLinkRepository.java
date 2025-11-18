package backend.knowhow.domain.member.repository;

import backend.knowhow.domain.member.domain.GuardianLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GuardianLinkRepository extends JpaRepository<GuardianLink, Long> {

    boolean existsByGuardianIdAndSeniorId(Long guardianId, Long seniorId);

    List<GuardianLink> findByGuardianId(Long guardianId);

    List<GuardianLink> findBySeniorId(Long seniorId);
}
