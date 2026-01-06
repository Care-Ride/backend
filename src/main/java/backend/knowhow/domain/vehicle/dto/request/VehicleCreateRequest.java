package backend.knowhow.domain.vehicle.dto.request;

public record VehicleCreateRequest(
        String name,
        String carNumber
) {
}
