package backend.knowhow.domain.member.repository;

import backend.knowhow.domain.member.domain.Member;
import backend.knowhow.domain.member.domain.MemberDeviceSetting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberDeviceSettingRepository extends JpaRepository<MemberDeviceSetting, Long> {

    Optional<MemberDeviceSetting> findByMemberAndDeviceId(Member member, String deviceId);
}
