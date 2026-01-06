package backend.knowhow.domain.vehicle.dto.request;

import jakarta.validation.constraints.NotBlank;

public record VehicleCreateRequest(
        @NotBlank String name,
        @NotBlank String carNumber
) {
}
