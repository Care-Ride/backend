package backend.knowhow.domain.driving.dto.response;

import backend.knowhow.domain.driving.dto.summary.DrivingSessionSummary;

import java.time.LocalDate;
import java.util.List;

public record DailyDrivingListResponse(
        List<DrivingSessionSummary> drive,
        LocalDate date
){}
