package backend.knowhow.domain.member.repository;

import backend.knowhow.domain.auth.domain.SocialType;
import backend.knowhow.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findBySocialTypeAndSocialId(SocialType socialType, String socialId);
}
