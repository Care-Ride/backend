package backend.knowhow.domain.driving.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class DriveStartResponse {
    private Long driveId;
    private LocalDateTime startTime;
}