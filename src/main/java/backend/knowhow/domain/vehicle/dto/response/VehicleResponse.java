package backend.knowhow.domain.vehicle.dto.response;

import backend.knowhow.domain.vehicle.domain.Vehicle;

public record VehicleResponse(Long id, String name, String carNumber, String bleDeviceId, boolean active) {
    public static VehicleResponse from(Vehicle vehicle) {
        return new VehicleResponse(
                vehicle.getId(),
                vehicle.getName(),
                vehicle.getCarNumber(),
                vehicle.getBleDeviceId(),
                vehicle.isActive()
        );
    }
}
