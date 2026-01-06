package backend.knowhow.domain.vehicle.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BleBindRequest {
    private String bleDeviceId;
}
