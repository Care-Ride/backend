package backend.knowhow.domain.vehicle.dto.request;

public record VehicleUpdateRequest(
        String name,
        String carNumber
) {}
