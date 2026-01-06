package backend.knowhow.domain.vehicle.dto;

public record VehicleCreateRequest(
        String name,
        String carNumber,
        String bleDeviceId
) {
}
