package backend.knowhow.domain.auth.repository;

import backend.knowhow.domain.auth.domain.GuardianLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GuardianLinkRepository extends JpaRepository<GuardianLink, Long> {

    boolean existsByGuardianIdAndSeniorId(Long guardianId, Long seniorId);

    List<GuardianLink> findByGuardianId(Long guardianId);

    List<GuardianLink> findBySeniorId(Long seniorId);
}
