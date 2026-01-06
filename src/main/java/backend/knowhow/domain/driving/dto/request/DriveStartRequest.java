package backend.knowhow.domain.driving.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DriveStartRequest {
    private double lat;
    private double lon;
    // BLE 연결 여부
    private boolean bleConnected;
}
