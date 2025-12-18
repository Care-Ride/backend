package backend.knowhow.domain.member.repository;

import backend.knowhow.domain.member.domain.GuardianLink;
import backend.knowhow.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GuardianLinkRepository extends JpaRepository<GuardianLink, Long> {

    boolean existsByGuardianAndSenior(Member guardian, Member senior);

    Optional<GuardianLink> findByGuardianIdAndRelationTypeIsNull(Long guardianId);

    Optional<GuardianLink> findByGuardianId(Long guardianId);
    Optional<GuardianLink> findBySeniorId(Long seniorId);
}
