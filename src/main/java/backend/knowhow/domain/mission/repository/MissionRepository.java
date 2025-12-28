package backend.knowhow.domain.mission.repository;

import backend.knowhow.domain.mission.domain.Mission;
import backend.knowhow.domain.mission.domain.MissionCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MissionRepository extends JpaRepository<Mission, Long> {
    Optional<Mission> findByCode(MissionCode code);
}