package backend.knowhow.domain.driving.repository;

import backend.knowhow.domain.driving.domain.DrivingSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface DrivingSessionRepository extends JpaRepository<DrivingSession, Long> {
    List<DrivingSession> findAllByDriverIdAndStartTimeGreaterThanEqualAndStartTimeLessThanAndEndTimeIsNotNull(
            Long driverId,
            LocalDateTime start,
            LocalDateTime end
    );

    List<DrivingSession> findAllByDriverIdAndStartTimeBetweenAndEndTimeIsNotNull(
            Long driverId,
            LocalDateTime startTime,
            LocalDateTime endTime
    );

    void deleteByDriver_Id(Long memberId);
}
