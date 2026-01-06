package backend.knowhow.domain.vehicle.dto;

public record VehicleUpdateRequest(
        String name,
        String carNumber
) {}
