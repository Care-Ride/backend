package backend.knowhow.domain.vehicle.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BleBindRequest {
    @NotBlank
    private String bleDeviceId;
}
