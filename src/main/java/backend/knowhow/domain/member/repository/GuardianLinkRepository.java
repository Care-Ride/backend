package backend.knowhow.domain.member.repository;

import backend.knowhow.domain.member.domain.GuardianLink;
import backend.knowhow.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuardianLinkRepository extends JpaRepository<GuardianLink, Long> {

    boolean existsByGuardianAndSenior(Member guardian, Member senior);
}
