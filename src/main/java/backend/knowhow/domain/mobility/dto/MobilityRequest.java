package backend.knowhow.domain.mobility.dto;

import backend.knowhow.domain.mobility.domain.MobilityType;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record MobilityRequest(
        @NotNull @DecimalMin("-90.0") @DecimalMax("90.0") double lat,
        @NotNull @DecimalMin("-180.0") @DecimalMax("180.0") double lon,
        @NotNull MobilityType mobilityType
) {}
