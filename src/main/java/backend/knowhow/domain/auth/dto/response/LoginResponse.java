package backend.knowhow.domain.auth.dto.response;

public record LoginResponse(
        String accessToken,
        String refreshToken,
        boolean hasDeviceSetting
) { }