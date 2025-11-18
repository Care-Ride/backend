package backend.knowhow.domain.auth.dto.response;

public record AuthResponse(
        String accessToken,
        String refreshToken
) { }
