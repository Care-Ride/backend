package backend.knowhow.domain.driving.repository;

import backend.knowhow.domain.driving.domain.DrivingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface DrivingSessionRepository extends JpaRepository<DrivingSession, Long> {
    List<DrivingSession> findAllByDriverIdAndStartTimeGreaterThanEqualAndStartTimeLessThanAndEndTimeIsNotNull(
            Long driverId,
            LocalDateTime start,
            LocalDateTime end
    );

    @Query("""
    SELECT ds
    FROM DrivingSession ds
    WHERE ds.driver.id = :driverId
      AND ds.startTime BETWEEN :startTime AND :endTime
      AND ds.endTime IS NOT NULL
      AND ds.score >= 0
""")
    List<DrivingSession> findValidDrivingSessions(
            @Param("driverId") Long driverId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    void deleteByDriver_Id(Long memberId);
}
