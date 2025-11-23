package backend.knowhow.domain.driving.repository;

import backend.knowhow.domain.driving.domain.DrivingSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DrivingSessionRepository extends JpaRepository<DrivingSession, Long> {
}
