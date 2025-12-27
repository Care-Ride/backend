package backend.knowhow.domain.driving.dto.response;

public record MonthlyDriveResponse(
        int month,
        double avgScore,
        int hardAccelStar,
        int hardDecelStar
){}
