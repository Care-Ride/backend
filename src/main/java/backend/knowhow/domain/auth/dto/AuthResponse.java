package backend.knowhow.domain.auth.dto;

public record AuthResponse(
        Long memberId,
        String nickname,
        String jwt
) { }
